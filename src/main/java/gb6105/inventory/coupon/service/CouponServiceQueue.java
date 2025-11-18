package gb6105.inventory.coupon.service;

import gb6105.inventory.coupon.domain.CouponIssueHistory.IssueStatus;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponServiceQueue {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisService redisService;
    private final RedissonClient redissonClient;
    private final CouponService couponService;
    private final CouponServiceRedis couponServiceRedis;

    private static final String COUPON_ISSUE_QUEUE_KEY = "coupon:issue:queue";
    private static final String COUPON_LOCK_PREFIX = "lock:coupon:";
    private static final String ISSUED_MEMBER_SET_PREFIX = "coupon:issued:";

    private final AtomicBoolean isWorkerRunning = new AtomicBoolean(false);

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startQueueWorker() {
        // 별도의 스레드를 생성하여 무한 루프를 백그라운드에서 실행합니다.
        if (isWorkerRunning.compareAndSet(false, true)) {
            new Thread(this::processCouponQueue).start();
            System.out.println("Coupon Queue Worker가 백그라운드에서 시작되었습니다.");
        } else {
            System.out.println("Coupon Queue Worker는 이미 실행 중입니다.");
        }
    }

    // 큐 모니터링: 별도의 스레드/태스크로 실행되어야 함.
    public void processCouponQueue() {
        while (true) {
            try {
                // BLPOP (Blocking Left Pop): 큐에 메시지가 들어올 때까지 대기
                String message = redisTemplate.opsForList()
                        .leftPop(COUPON_ISSUE_QUEUE_KEY, 10, TimeUnit.SECONDS);
                if (message != null) {
                    String[] parts = message.split(":");
                    String email = parts[0];
                    Long couponId = Long.parseLong(parts[1]);
                    Long timestamp = Long.parseLong(parts[2]);
                    System.out.println("큐 요청 시간" + timestamp);

                    couponServiceRedis.issueCouponWithRedis(email,couponId);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void issueCouponSequentially(String email, Long couponId) {

        String key = ISSUED_MEMBER_SET_PREFIX + couponId;
        RSet<String> issuedMembers = redissonClient.getSet(key);

        // redis에서 발급 이력을 확인
        if(issuedMembers.contains(email)) {
            System.out.println("이미 쿠폰을 발급 받았습니다." + email);
            throw new IllegalStateException("이미 쿠폰을 발급 받았습니다.");
        }

        //분산 락 로직
        String lockName = COUPON_LOCK_PREFIX + couponId;
        RLock lock = redissonClient.getLock(lockName);
        long waitTime = 1; // 락 획득 대기 시간 (짧게 설정)
        long leastTime = 3; // 락 유지 시간 (트랜잭션 시간보다 길게 설정)
        TimeUnit time = TimeUnit.SECONDS;

        try {
            boolean isLocked = lock.tryLock(waitTime, leastTime, time);

            if (!isLocked) {
                couponService.saveIssueResult(email, couponId, IssueStatus.FAIL.getMessage());
                System.out.println("락 획득 실패: " + email);
                return;
            }

            // 락 획득 성공: DB 트랜잭션 실행
            couponService.issueCouponCore(email,couponId);
            System.out.println("✅ 순서 처리 성공: " + email + " | 락 해제");

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("처리 실패: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("락 획득 중 인터럽트");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

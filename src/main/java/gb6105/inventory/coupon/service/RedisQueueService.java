package gb6105.inventory.coupon.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisQueueService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String COUPON_ISSUE_QUEUE_KEY = "coupon:issue:queue";

    public void enqueueCouponIssueRequest(String email, Long couponId) {
        // 요청을 큐에 담은 시간을 포함시킴
        long timestamp = Instant.now().toEpochMilli();
        String requestMessage = String.format("%s:%d:%d", email, couponId, timestamp);

        // 큐에 요청을 push
        redisTemplate.opsForList().rightPush(COUPON_ISSUE_QUEUE_KEY, requestMessage);
    }

}

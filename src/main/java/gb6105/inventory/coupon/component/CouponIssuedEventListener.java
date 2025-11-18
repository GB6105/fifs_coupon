package gb6105.inventory.coupon.component;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CouponIssuedEventListener {

    private final RedissonClient redissonClient;
    private static final String ISSUED_MEMBER_SET_PREFIX = "coupon:issued:";


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCouponIssuedEvent(CouponIssuedEvent event) {
        String key = ISSUED_MEMBER_SET_PREFIX + event.getCouponId();
        RSet<String> issuedMembers = redissonClient.getSet(key);

        // Redis Set에 이메일 추가 (SADD)
        issuedMembers.add(event.getEmail());

        System.out.println("트랜잭션 커밋 후 Redis Set에 성공적으로 반영됨: " + event.getEmail());
    }
}


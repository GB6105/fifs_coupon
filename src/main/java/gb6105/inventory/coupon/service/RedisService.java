package gb6105.inventory.coupon.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;
    private static final String COUNPON_STOCK_PREFIX = "coupon:stock:";
    private static final String ISSUED_MEMBER_SET_PREFIX = "coupon:issued:";

    public void initializeStock(Long couponId, int quantity) {
        redisTemplate.opsForValue().set(COUNPON_STOCK_PREFIX + couponId, String.valueOf(quantity));
    }

    public void decreaseStock(Long couponId) {
        redisTemplate.opsForValue().decrement(COUNPON_STOCK_PREFIX + couponId);
    }

    public int getCurrentStock(Long CouponId) {
        return Integer.parseInt(redisTemplate.opsForValue().get(COUNPON_STOCK_PREFIX + CouponId));
    }

    public Set<String> getIssuedMembers(Long couponId) {
        String key = ISSUED_MEMBER_SET_PREFIX + couponId;

        RSet<String> issuedMembers = redissonClient.getSet(key);

        return issuedMembers.readAll();
    }
}

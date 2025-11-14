package gb6105.inventory.coupon.data;

import gb6105.inventory.coupon.domain.Coupon;
import gb6105.inventory.coupon.domain.Member;
import gb6105.inventory.coupon.repository.CouponRepository;
import gb6105.inventory.coupon.repository.MemberRepository;
import gb6105.inventory.coupon.service.RedisStockService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final RedisStockService redisStockService;

    public DataInitializer(MemberRepository memberRepository, CouponRepository couponRepository , RedisStockService redisStockService) {
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
        this.redisStockService = redisStockService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        if(couponRepository.findById(1L).isEmpty()){
            couponRepository.save(new Coupon("coffee",10));
        }

        redisStockService.initializeStock(1L,10);

        if (memberRepository.count() == 0) { // 데이터가 없을 때만 실행
            for (int i = 1; i <= 100; i++) {
                String email = String.format("user_%03d@test.com", i);
                memberRepository.save(new Member(email));
            }
            System.out.println("⭐ 100명의 테스트 회원을 생성했습니다.");
        }
    }
}

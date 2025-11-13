package gb6105.inventory.coupon.service;


import gb6105.inventory.coupon.domain.Coupon;
import gb6105.inventory.coupon.repository.CouponRepository;
import gb6105.inventory.coupon.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.client.protocol.convertor.VoidReplayConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private CouponRepository couponRepository;
    private IssuedCouponRepository issuedCouponRepository;

    @Transactional
    public void issueCoupon(Coupon coupon) {
        // TODO
        // issuedCoupon에서 발급 여부 조회

    }

}

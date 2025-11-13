package gb6105.inventory.coupon.repository;

import gb6105.inventory.coupon.domain.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Integer> {


}

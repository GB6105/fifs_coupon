package gb6105.inventory.coupon.repository;

import gb6105.inventory.coupon.domain.Coupon;
import gb6105.inventory.coupon.domain.CouponIssueHistory;
import gb6105.inventory.coupon.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueHistoryRepository extends JpaRepository<CouponIssueHistory, Long> {
    long countByMemberAndCouponAndStatus(Member member, Coupon coupon, CouponIssueHistory.IssueStatus status);
}

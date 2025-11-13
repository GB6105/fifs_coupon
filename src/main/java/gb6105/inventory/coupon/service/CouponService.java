package gb6105.inventory.coupon.service;


import gb6105.inventory.coupon.domain.Coupon;
import gb6105.inventory.coupon.domain.CouponIssueHistory;
import gb6105.inventory.coupon.domain.CouponIssueHistory.IssueStatus;
import gb6105.inventory.coupon.domain.Member;
import gb6105.inventory.coupon.repository.CouponRepository;
import gb6105.inventory.coupon.repository.CouponIssueHistoryRepository;
import gb6105.inventory.coupon.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private MemberRepository memberRepository;
    private CouponRepository couponRepository;
    private CouponIssueHistoryRepository historyRepository;

    @Transactional
    public void issueCoupon(String email, Long couponId) {
        // TODO
        // issuedCoupon에서 발급 여부 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰 정보를 찾을수 없습니다."));

        long existCouponCount = historyRepository.countByMemberAndCouponStatus(member, coupon, IssueStatus.SUCCESS);

        if(existCouponCount > 0) {
            throw new IllegalArgumentException("이미 쿠폰을 발급받았습니다.");
        }

        if(coupon.getTotal_quantity() > 0){
            coupon.decreaseQuantity();
            couponRepository.save(coupon);

            saveIssueResult(member,coupon,IssueStatus.SUCCESS);
        }else{
            saveIssueResult(member,coupon,IssueStatus.FAIL);
            throw new IllegalArgumentException("쿠폰 재고가 소진되었습니다.");
        }

    }

    private void saveIssueResult(Member member, Coupon coupon, CouponIssueHistory.IssueStatus status) {
        CouponIssueHistory history = new CouponIssueHistory(member,coupon,status);
        historyRepository.save(history);
    }

}

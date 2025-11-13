package gb6105.inventory.coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CouponIssueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    private Member member_id;
    private Coupon coupon_id;

    private IssueStatus status;

    public enum IssueStatus {
        SUCCESS, FAIL
    }

    public CouponIssueHistory() {

    }

    public CouponIssueHistory(Member member_id, Coupon coupon_id, IssueStatus status) {
        this.member_id = member_id;
        this.coupon_id = coupon_id;
        this.status = status;
    }

}

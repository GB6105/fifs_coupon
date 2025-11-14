package gb6105.inventory.coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "coupon_issue_history")
public class CouponIssueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY) // 불필요한 데이터 로딩 방지
    @JoinColumn(name = "member_id", nullable = false) // DB 칼럼과 연관성
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private IssueStatus status;

    public enum IssueStatus {
        SUCCESS, FAIL
    }

    public CouponIssueHistory() {

    }

    /**
     *
     * @param member
     * @param coupon
     * @param status
     */
    public CouponIssueHistory(Member member, Coupon coupon, IssueStatus status) {
        this.member= member;
        this.coupon = coupon;
        this.status = status;
    }

}

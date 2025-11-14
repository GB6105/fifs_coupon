package gb6105.inventory.coupon.dto;

public record CouponIssueRequest(
        String email,
        Long couponId
) {
}

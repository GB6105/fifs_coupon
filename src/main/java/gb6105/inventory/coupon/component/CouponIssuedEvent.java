package gb6105.inventory.coupon.component;

public record CouponIssuedEvent(String email, Long couponId) {
    public String getCouponId() {
        return couponId.toString();
    }

    public String getEmail() {
        return email;
    }
}


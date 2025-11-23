package gb6105.inventory.coupon.dto;

public record StockResponse(
        Long couponId,
        int remainingStock
) {
}

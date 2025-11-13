package gb6105.inventory.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String name;

    private int total_quantity;

    public Coupon(Long couponId, String name, int total_quantity) {
        this.couponId = couponId;
        this.name = name;
        this.total_quantity = total_quantity;
    }

    public void decreaseQuantity() {
        if(this.total_quantity < 0) {
            throw new RuntimeException("쿠폰이 모두 소진되었습니다.");
        }
        total_quantity--;
    }

}

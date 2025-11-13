package gb6105.inventory.coupon.repository;

import gb6105.inventory.coupon.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
        Optional<Member> findByEmail(String email);
}

package gb6105.inventory.coupon.service;

import gb6105.inventory.coupon.domain.Member;
import gb6105.inventory.coupon.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void registerMember(String email) {
        Member member = new Member(email);
        if(!memberRepository.findByEmail(email).isPresent()) {
            memberRepository.save(member);
        }
    }
}

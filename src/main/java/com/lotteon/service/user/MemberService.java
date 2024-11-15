package com.lotteon.service.user;

import com.lotteon.entity.User.Delivery;
import com.lotteon.entity.User.Member;
import com.lotteon.repository.user.DeliveryRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final EmailService emailService;
    private final HttpSession session;


    // 모든 회원 목록 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // id로 특정 회원 조회
    public Optional<Member> getMemberByUid(Long id) {
        return memberRepository.findById(id);
    }

    // uid로 특정회원 조회
    public Optional<Member> findByUserId(String uid) {
        return memberRepository.findByUser_Uid(uid);
    }

    // 회원 정보 수정
    @Transactional
    public void updateMember(Long id, Member updatedMember) {
        Optional<Member> existingMemberOpt = memberRepository.findById(id);
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();
            // 기존 회원 정보 업데이트
            existingMember.setName(updatedMember.getName());
            existingMember.setGender(updatedMember.getGender());
            existingMember.setEmail(updatedMember.getEmail());
            existingMember.setHp(updatedMember.getHp());
            existingMember.setPostcode(updatedMember.getPostcode());
            existingMember.setAddr(updatedMember.getAddr());
            existingMember.setAddr2(updatedMember.getAddr2());
            existingMember.setUserinfocol(updatedMember.getUserinfocol());

            existingMember.setStatus(updatedMember.getStatus());
            existingMember.setGrade(updatedMember.getGrade());

            memberRepository.save(existingMember);
        }
    }

    // 회원 삭제
    @Transactional
    public void deleteMembersByIds(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            Optional<Member> memberOptional = memberRepository.findById(memberId);
            if (memberOptional.isPresent()) {
                // Seller가 존재하면 삭제
                memberRepository.delete(memberOptional.get());
            } else {
                throw new EntityNotFoundException("일치하는 아이디의 seller가 없습니다.: " + memberId);
            }
        }
    }

    public List<Member> findAllWithPoints(){
        return memberRepository.findAllWithPoints();
    }

    private final Map<String, String> memberDatabase = new HashMap<>();

    public String sendVerificationCode(String email) {
        // 사용자 이메일을 통해 인증 코드 발송
        return emailService.sendVerifyEmail(email);
    }

    public Member findIdByNameAndEmail(String name, String email) {
        return memberRepository.findByNameAndEmail(name, email)
                .orElse(null); // 전체 Member 객체를 반환하고, 없으면 null 반환
    }


    public Member verifyCodeForUser(String verificationCode, String name, String email) {
        // 1. 세션에서 저장된 인증번호 및 사용자 정보 가져오기
        String sessionCode = (String) session.getAttribute("code");  // 세션에 저장된 인증번호 가져오기
        String sessionName = (String) session.getAttribute("name");
        String sessionEmail = (String) session.getAttribute("email");

        // 2. 검증: 세션에 저장된 인증번호와 사용자 정보가 입력된 값과 일치하는지 확인
        if (sessionCode == null || !sessionCode.equals(verificationCode)
                || !sessionName.equals(name) || !sessionEmail.equals(email)) {
            throw new RuntimeException("인증번호가 일치하지 않거나 사용자 정보가 일치하지 않습니다.");
        }

        // 3. 검증 성공 후, 유저의 아이디 반환
        Member member = memberRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new RuntimeException("해당 이름과 이메일로 계정을 찾을 수 없습니다."));

        return member;  // 유저의 아이디 반환
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Delivery> getDeliveryByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        return member.getDeliveryList();
    }

    public Delivery addDeliveryToMember(Long memberId, Delivery delivery) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        member.addDelivery(delivery);
        return deliveryRepository.save(delivery);
    }
}
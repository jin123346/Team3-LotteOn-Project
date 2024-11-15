package com.lotteon.controller;

import com.lotteon.dto.User.Grade;
import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.User.UserDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.MemberStatus;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.UserRepository;
import com.lotteon.entity.User.MemberStatus;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.PointService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static com.lotteon.entity.User.QUser.user;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    private final MemberService memberService;
    private final UserService userService;
    private final PointService pointService;

    @GetMapping("/list")
    public String adminUserList(Model model) {

        model.addAttribute("cate", "user");
        model.addAttribute("content", "list");

        List<Member> memberList = memberService.getAllMembers();

        memberList.forEach(member -> {
            if (member.getGrade() == null) {
                member.setGrade(Grade.FAMILY); // null 처리하여 기본값 적용
            }
        });

        memberList.forEach(member -> log.info("Member ID: {}, Grade: {}", member.getId(), member.getGrade()));


        log.info("회원 목록: {}", memberList);
        model.addAttribute("memberList", memberList);
        return "content/admin/user/memberlist";
    }

    @GetMapping("/{uid}")
    public ResponseEntity<Member> getUserById(@PathVariable String uid) {
        Optional<Member> optionalMember = memberService.findByUserId(uid);
        if (optionalMember.isPresent()) {
            return ResponseEntity.ok(optionalMember.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 사용자 없음
        }
    }

    @PostMapping("/deletemembers")
    public ResponseEntity<String> deleteMembers(@RequestBody List<Long> memberIds) {
        System.out.println("Received member IDs: " + memberIds); // 로그 출력
        try {
            memberService.deleteMembersByIds(memberIds);
            return ResponseEntity.ok("선택한 회원이 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }
    }


    @PostMapping("/member/update")
    public ResponseEntity<Map<String, String>> updateMember(@RequestParam String uid, @RequestBody MemberDTO memberDTO) {
        Optional<Member> existingMemberOpt = memberService.findByUserId(uid);
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();

            // MemberDTO를 기존 Member 객체에 업데이트
            existingMember.setName(memberDTO.getName());
            existingMember.setGender(memberDTO.getGender());
            existingMember.setEmail(memberDTO.getEmail());
            existingMember.setHp(memberDTO.getHp());
            existingMember.setPostcode(memberDTO.getPostcode());
            existingMember.setAddr(memberDTO.getAddr());
            existingMember.setAddr2(memberDTO.getAddr2());
            existingMember.setAddr(memberDTO.getAddr());
            existingMember.setAddr2(memberDTO.getAddr2());
            // 추가적인 필드가 있다면 여기서 업데이트

            // 업데이트 메서드 호출
            memberService.updateMember(existingMember.getId(), existingMember);
            // 메시지를 Map으로 감싸서 JSON 형태로 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/updateGrade")
    public ResponseEntity<Map<String, String>> updateGrade(@RequestBody Map<String, String> request) {
        String uid = request.get("uid"); // UID 가져오기
        String grade = request.get("grade"); // 새 등급 가져오기

        log.info("uid: " + uid + " grade: " + grade);

        Optional<Member> existingMemberOpt = memberService.findByUserId(uid);
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();
            existingMember.setGrade(Grade.valueOf(grade)); // 등급 업데이트
            memberService.updateMember(existingMember.getId(), existingMember); // 변경된 멤버 정보 저장

            // 성공적인 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 등급이 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/member/updateStatus")
    public ResponseEntity<Map<String, String>> updateStatus(@RequestBody Map<String, String> request) {
        String uid = request.get("uid"); // UID 가져오기
        String status = request.get("status"); // 새 등급 가져오기

        log.info("uid: " + uid + " status: " + status);

        Optional<Member> existingMemberOpt = memberService.findByUserId(uid);
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();
            existingMember.setStatus(MemberStatus.valueOf(status)); // 등급 업데이트
            memberService.updateMember(existingMember.getId(), existingMember); // 변경된 멤버 정보 저장

            // 성공적인 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 상태가 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/point")
    public String adminUserPoint(Model model) {
        List<Member> members = memberService.findAllWithPoints();

        model.addAttribute("members", members);
        log.info("Members with points: {}", members);

        for (Member member : members) {
            if (member.getPoints().isEmpty()) {
                Point defaultPoint = new Point();
                defaultPoint.setAmount(0);
                defaultPoint.setRemainingPoints(0);
                defaultPoint.setDescription("사용할 포인트가 없습니다.");
                member.getPoints().add(defaultPoint);
            }
        }
        model.addAttribute("cate", "user");
        model.addAttribute("content", "list");

        return "content/admin/user/memberpoint";
    }

    @PostMapping("/point/add")
    public ResponseEntity<Point> addPoint(@RequestParam Long memberId,
                                          @RequestParam int amount,
                                          @RequestParam String description) {

        log.info("Creating point for memberId: {}, amount: {}, description: {}", memberId, amount, description);

        Point newPoint = pointService.createPoint(memberId, amount, description);

        log.info("newPoint: " + newPoint);
        System.out.println("newPoint: " + newPoint);

        return ResponseEntity.ok(newPoint);
    }
}
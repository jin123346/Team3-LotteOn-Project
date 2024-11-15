package com.lotteon.controller.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEmailController {

    private final MemberService memberService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/findid")
    public String findid(Model model, HttpSession session) {
        // 세션에서 member 객체 가져오기
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        return "content/user/findid";
    }

    @PostMapping("/findid")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        String email = requestBody.get("email");

        Member member = memberService.findIdByNameAndEmail(name, email);

        System.out.println("findId 호출됨: name=" + name + ", email=" + email); // 로그 추가

        if (member != null) {
            // 사용자 정보를 JSON 형식으로 반환
            return ResponseEntity.ok((member));  // 사용자의 ID만 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");  // 404 NOT FOUND와 함께 오류 메시지 반환
        }
    }

    @GetMapping("/findresult")
    public String findResult(@RequestParam(name = "memberId", required = false) Long memberId, Model model) {
        System.out.println("findResult 호출됨: memberId=" + memberId); // 디버깅 로그 추가

        if (memberId == null) {
            System.out.println("memberId가 null입니다.");
            return "redirect:/user/findid?error=missingId"; // 에러가 발생했을 때 리다이렉트
        }

        Optional<Member> optionalMember = memberService.findById(memberId);

        if (optionalMember.isPresent()) {
            model.addAttribute("member", optionalMember.get());
            return "content/user/findresult";
        } else {
            System.out.println("해당 ID의 사용자가 존재하지 않습니다: " + memberId);
            return "redirect:/user/findid?error=userNotFound";
        }
    }

    @GetMapping("/findpass")
    public String findpass(Model model, HttpSession session) {
        // 세션에서 member 객체 가져오기
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        return "content/user/findpass";
    }

    @PostMapping("/findpass")
    public ResponseEntity<?> findpass(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        String email = requestBody.get("email");

        Member member = memberService.findIdByNameAndEmail(name, email);

        System.out.println("findId 호출됨: name=" + name + ", email=" + email); // 로그 추가

        if (member != null) {
            // 사용자 정보를 JSON 형식으로 반환
            return ResponseEntity.ok((member));  // 사용자의 ID만 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");  // 404 NOT FOUND와 함께 오류 메시지 반환
        }
    }

    @GetMapping("/changepass")
    public String changepass(@RequestParam(name = "memberId", required = false) Long memberId, Model model) {
        System.out.println("changepass 호출됨: memberId=" + memberId); // 디버깅 로그 추가

        if (memberId == null) {
            System.out.println("memberId가 null입니다.");
            return "redirect:/user/findpass?error=missingId"; // 에러가 발생했을 때 리다이렉트
        }

        Optional<Member> optionalMember = memberService.findById(memberId);

        if (optionalMember.isPresent()) {
            model.addAttribute("member", optionalMember.get());
            return "content/user/changepass";
        } else {
            System.out.println("해당 ID의 사용자가 존재하지 않습니다: " + memberId);
            return "redirect:/user/findpass?error=userNotFound";
        }
    }

    @PostMapping("/changepass")
    public String resetPassword(@RequestParam String uid, // 사용자 ID
                                @RequestParam String password, // 새 비밀번호
                                RedirectAttributes redirectAttributes) {

        System.out.println("전달된 userId: " + uid); // 디버깅용 로그
        System.out.println("전달된 password: " + password); // 디버깅용 로그

        Optional<User> optionalUser = userService.findUserByUid(uid); // 사용자 ID로 사용자 조회

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = passwordEncoder.encode(password); // 비밀번호 인코딩
            user.setPass(encodedPassword); // user의 인코딩된 비밀번호 변경
            userService.save(user); // 비밀번호 저장
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉션
        } else {
            redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
            return "redirect:/user/changepass"; // 비밀번호 재설정 요청 페이지로 리다이렉션
        }
    }
}



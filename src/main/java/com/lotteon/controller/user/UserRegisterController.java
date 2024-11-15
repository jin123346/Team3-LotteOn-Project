package com.lotteon.controller.user;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.service.user.DeliveryService;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryService deliveryService;
    private final MemberService memberService;

    @GetMapping("/memberregister")
    public String memberRegister(Model model) {
        model.addAttribute("content", "memberregister");
        return "content/user/memberregister"; // Points to "content/user/memberregister"
    }

    @PostMapping("/memberregister")
    public String registerMember(@ModelAttribute User user, @ModelAttribute Member member) {
        // 비밀번호 인코딩
        // 인코딩된 비밀번호 설정


        userService.registerUserAndMember(user, member);

        log.info("user: " + user + " member: " + member);
        return "redirect:/user/login";
    }


//    @PostMapping("/memberregister")
//    public String registerMember(@ModelAttribute User user, @ModelAttribute Member member) {
//        // 비밀번호 인코딩
//        String encodedPassword = passwordEncoder.encode(user.getPass());
//        user.setPass(encodedPassword);  // 인코딩된 비밀번호 설정
//
//        log.info("Encoded Password: " + encodedPassword);
//
//        Member savedmember= userService.registerMember(user, member);
////        log.info("user: " + user + " member: " + member);
//
//        return "redirect:/user/delivery/save/"+savedmember.getUid();
//    }
//
//    @GetMapping("/user/delivery/save/{uid}")
//    public String saveTOdelivery(@PathVariable String uid, Model model) {
//                Optional<Member> opt = memberService.findByUserId(uid);
//                if(opt.isPresent()) {
//                    Member member = opt.get();
//                    deliveryService.saveDefaultDelivery(member);
//                    return "redirect:/user/login?success=true";
//
//                }
//
//        return "redirect:/user/login?success=false";
//
//
//    }

    @GetMapping("/sellerregister")
    public String sellerRegister(Model model) {
        model.addAttribute("content", "sellerregister");
        return "content/user/sellerregister"; // Points to "content/user/sellerregister"
    }

    @PostMapping("/sellerregister")
    public String registerSeller(@ModelAttribute User user, @ModelAttribute Seller seller) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getPass());
        user.setPass(encodedPassword);  // 인코딩된 비밀번호 설정

        log.info("Encoded Password: " + encodedPassword);  // 인코딩된 비밀번호 로그

        userService.registerSeller(user, seller);
        log.info("user: " + user + " seller: " + seller);
        return "redirect:/user/login";
    }

    @PostMapping("/sellerregister2")
    public String registerSeller2(@ModelAttribute User user, @ModelAttribute Seller seller) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getPass());
        user.setPass(encodedPassword);  // 인코딩된 비밀번호 설정

        log.info("Encoded Password: " + encodedPassword);  // 인코딩된 비밀번호 로그

        userService.registerSeller(user, seller);
        log.info("user: " + user + " seller: " + seller);
        return "redirect:/admin/store/shoplist";
    }

    @ResponseBody
    @GetMapping("/checkUser")
    public ResponseEntity<Map<String, Object>> checkUser(@RequestParam String type, @RequestParam String value) {
        Map<String, Object> response = new HashMap<>();
        boolean exists;

        switch (type) {
            case "uid":
                exists = userService.checkUserId(value); // 아이디 중복 체크 로직
                break;
            case "email":
                exists = userService.checkEmail(value); // 이메일 중복 체크 로직
                break;
            case "hp":
                exists = userService.checkPhone(value); // 휴대폰 중복 체크 로직
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        response.put("result", exists ? 1 : 0); // 존재하면 1, 존재하지 않으면 0
        return ResponseEntity.ok(response);
    }
}

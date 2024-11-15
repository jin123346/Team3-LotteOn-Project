package com.lotteon.controller.user;

import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.security.InactiveUserException;
import com.lotteon.service.AdminService;
import com.lotteon.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    @GetMapping("/join")
    public String userJoin(Model model) {
        model.addAttribute("content", "join");
        return "content/user/join"; // Points to "content/user/join"
    }

    @GetMapping("/login")
    public String userLogin(Model model, HttpServletRequest request, @RequestParam(value = "redirect", required = false) String redirectUrl) {
        if (redirectUrl != null) {
            request.getSession().setAttribute("redirectUrl", redirectUrl);

        }
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("banners", banners2);

        model.addAttribute("content", "login");
        return "content/user/login"; // Points to "content/user/login"
    }

    @GetMapping("/term")
    public String terms(Model model) {
        model.addAttribute("content", "term");
        return "content/user/term"; // Points to "content/user/term"
    }

    @PostMapping("/loginValid")
    public ResponseEntity<Map<String, Object>> loginValid(@RequestBody Map<String, String> requestBody) {
        String uid = requestBody.get("inId"); // 클라이언트에서 사용한 키와 일치시켜야 합니다
        String password = requestBody.get("password"); // 클라이언트에서 사용한 키와 일치시켜야 합니다

        System.out.println("uid: " + uid);
        System.out.println("password: " + password);

        Map<String, Object> response = new HashMap<>();
        boolean loginSuccess = userService.login(uid, password); // 로그인 성공 여부를 boolean으로 받음

        if (loginSuccess) {
            response.put("success", true);
            response.put("message", "로그인 성공");
            return ResponseEntity.ok(response); // 200 OK 응답 반환
        } else {
            response.put("success", false);
            response.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized 응답 반환
        }
    }


    @PostMapping("/login")
    public String login(@RequestParam("inId") String username,
                        @RequestParam("password") String password, HttpServletRequest request
            , Model model) {

        try {
            if (userService.login(username, password)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                HttpSession session = request.getSession();
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());// 세션에 인증 정보 저장


                // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
                String memberName = userService.getMemberNameByUsername(username);
                log.info("login성공!!!!!!" + memberName);
                model.addAttribute("memberName", memberName);


                String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    request.getSession().removeAttribute("redirectUrl"); // Clear session attribute
                    return "redirect:" + redirectUrl;
                }

                return "redirect:/?success=100"; // 로그인 성공 후 이동할 페이지
            } else {

                return "redirect:/user/login?error";
            }

        } catch (InactiveUserException e) {
            // InactiveUserException 발생 시 경고 메시지를 모델에 추가하여 alert 표시
            model.addAttribute("errorMessage", e.getMessage());
            return "user/login";  // 로그인 페이지로 이동하며 메시지 표시
        } catch (BadCredentialsException e) {
            return "redirect:/user/login?error";
        }
    }

}


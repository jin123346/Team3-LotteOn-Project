package com.lotteon.controller.user;

import com.lotteon.dto.admin.TermsDto;
import com.lotteon.entity.admin.Terms;
import com.lotteon.service.admin.TermsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lotteon.entity.admin.Terms.TermsType; // TermsType enum import


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@RestController
public class TermsController {

    private final TermsService termsService;

    @GetMapping("/terms")
    public String terms(@RequestParam String type, Model model) {
        // 모든 약관을 조회
        List<Terms> allTerms = termsService.findAllTerms();
        log.info(allTerms);


//         사용자 타입에 따라 필터링
        List<Terms> termsList;
        if ("member".equalsIgnoreCase(type)) {
            termsList = allTerms.stream()
                    .filter(term -> "BUYER".equals(term.getType()) ||
                            "ELECTRONIC_FINANCE".equals(term.getType()) ||
                            "LOCATION_INFO".equals(term.getType()) ||
                            "PRIVACY_POLICY".equals(term.getType()))
                    .collect(Collectors.toList());
        } else if ("seller".equalsIgnoreCase(type)) {
            termsList = allTerms.stream()
                    .filter(term -> "SELLER".equals(term.getType()) ||
                            "ELECTRONIC_FINANCE".equals(term.getType()) ||
                            "PRIVACY_POLICY".equals(term.getType()))
                    .collect(Collectors.toList());
        } else {
            termsList = List.of(); // 기본적으로 빈 리스트
        }

        log.info(termsList);

        model.addAttribute("termsList", termsList); // 필터링된 약관 리스트를 모델에 추가
        model.addAttribute("userType", type); // 사용자 타입도 추가
        return "/terms"; // terms.html 페이지로 이동
    }




    @PostMapping("/terms")
    public String acceptTerms(
            @RequestParam String type,
            @RequestParam String someField,
            HttpSession session) {

        // 세션에 값 저장
        session.setAttribute("someField", someField);
        session.setAttribute("userType", type); // 회원 유형도 세션에 저장

        System.out.println("Received someField: " + someField);

        // 회원 유형에 따라 리다이렉트
        if ("member".equals(type)) {
            return "redirect:/user/memberregister"; // 개인 회원가입 폼으로 리다이렉트
        } else if ("seller".equals(type)) {
            return "redirect:/user/sellerregister"; // 셀러 회원가입 폼으로 리다이렉트
        }

        return "redirect:/signup"; // 기본값 (오류 처리용)
    }

    @GetMapping("/api/terms")
    public List<Terms> getTerms() {
        return termsService.getAllTerms();
    }

}

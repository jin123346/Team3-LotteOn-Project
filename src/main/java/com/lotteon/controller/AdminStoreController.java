package com.lotteon.controller;


import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.service.user.SellerService;
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

@Log4j2
@Controller
@RequestMapping("/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final SellerService sellerService;

    @GetMapping("/shoplist")
    public String adminShoplist(Model model) {



        List<Seller> sellerList = sellerService.getAllSellers();
        model.addAttribute("sellerList", sellerList);

        model.addAttribute("cate", "store");
        model.addAttribute("content", "shoplist");

        log.info("admin shoplist: " + sellerList);

        return "content/admin/shop/admin_shoplist";
    }

    @PostMapping("/updateStatus")
    public  ResponseEntity<Map<String, String>> updateSeller(@RequestParam String uid, @RequestBody SellerDTO sellerDTO) {
        log.info("uid: "+ uid +"updateSeller: " + sellerDTO);
        Optional<User> existingSellerOpt = sellerService.findSellerByUid(uid);
                log.info(existingSellerOpt.toString());
        if (existingSellerOpt.isPresent()) {
            Seller existingSeller = existingSellerOpt.get().getSeller();

            // MemberDTO를 기존 Member 객체에 업데이트
            existingSeller.setStatus(sellerDTO.getStatus());

            // 추가적인 필드가 있다면 여기서 업데이트

            // 업데이트 메서드 호출
            sellerService.updateSeller(existingSeller.getId(), existingSeller);
            // 메시지를 Map으로 감싸서 JSON 형태로 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "상점 정보가 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/deletesellers")
    public ResponseEntity<String> deleteSellers(@RequestBody List<Long> sellerIds) {
        System.out.println("Received seller IDs: " + sellerIds); // 로그 출력
        try {
            sellerService.deleteSellersByIds(sellerIds);
            return ResponseEntity.ok("선택한 판매자가 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }
    }


    @GetMapping("/sale")
    public String adminShopsales(Model model) {
        model.addAttribute("cate", "store");
        model.addAttribute("content", "sale");
        return "content/admin/shop/admin_shopsales";
    }
}

package com.lotteon.controller.user;

import com.lotteon.dto.User.DeliveryDTO;
import com.lotteon.entity.User.Delivery;
import com.lotteon.entity.User.Member;
import com.lotteon.service.user.DeliveryService;
import com.lotteon.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/{memberId}")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final MemberService memberService;

    // 배송지 등록 엔드포인트
    @PostMapping("/delivery")
    public ResponseEntity<Delivery> registerDelivery(@PathVariable Long memberId, @RequestBody Delivery delivery) {

        System.out.println("registerDelivery: " + delivery.toString());
        try {
            Delivery savedDelivery = deliveryService.saveDelivery(memberId, delivery);
            return ResponseEntity.ok(savedDelivery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((Delivery) Collections.singletonMap("error", "배송지 등록 실패"));
        }
    }

    // 특정 회원의 배송지 목록 조회 엔드포인트
    @GetMapping("/deliveries")
    public ResponseEntity<Map<String, Object>> getDeliveries() {

        // 1. SecurityContextHolder에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName(); // 현재 로그인된 사용자의 UID 가져옴

        // 2. uid로 Member 조회하여 memberId 가져오기
        Member member = memberService.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        Long memberId = member.getId(); // Member ID 추출

        // 3. memberId로 배송지 목록 조회
        List<DeliveryDTO> deliveries = deliveryService.getByMemberId(memberId);

        // 4. 조회된 배송지 목록을 ResponseEntity로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("member", member); // 기본 주소 정보
        response.put("deliveries", deliveries); // 배송지 목록

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getDelivery(@PathVariable Long memberId, @PathVariable Long deliveryId) {
        DeliveryDTO delivery = deliveryService.getDeliveryById(memberId, deliveryId);
        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long memberId, @PathVariable Long deliveryId) {
        log.info("deleteDelivery: " + deliveryId);
        boolean deleted = deliveryService.deleteDelivery(deliveryId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/delivery/{deliveryId}/update")
    public ResponseEntity<Delivery> updateDelivery(
            @PathVariable Long memberId,
            @PathVariable Long deliveryId,
            @RequestBody Delivery deliveryData) {

        // 배송지 수정 처리
        Delivery updatedDelivery = deliveryService.updateDelivery(memberId, deliveryId, deliveryData);

        System.out.println("Updated Delivery: " + updatedDelivery);

        if (updatedDelivery == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(updatedDelivery);  // 수정된 배송지 정보 반환
    }
}

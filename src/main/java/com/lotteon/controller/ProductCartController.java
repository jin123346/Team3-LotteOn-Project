package com.lotteon.controller;


import com.lotteon.dto.order.CartOrderRequestDTO;
import com.lotteon.dto.product.cart.CartRequestDTO;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.service.order.CartItemService;
import com.lotteon.service.product.MarketCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductCartController {



    private final MarketCartService marketCartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;


    @GetMapping("/list")
    public ResponseEntity<List<CartItem>> selectCartAll(@AuthenticationPrincipal UserDetails userDetails) {

        log.info("리스트호출!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
       if(userDetails == null) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
       }

       try {
           List<CartItem> cartItems = marketCartService.selectCartAll();

           log.info("카트 총집합! cart items: {}", cartItems);

           return ResponseEntity.ok(cartItems);
       } catch (Exception e) {
           log.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }

    }


    @PostMapping("/cart")
    public ResponseEntity<Map<String, Object>> insertCart(@RequestBody List<BuyNowRequestDTO> productDataList) {
        log.info("호출됨0000000000000000000000000000");

        Map<String, Object> resp = new HashMap<>();
        BuyNowRequestDTO cartRequestDTO = productDataList.get(0);

        try {

            // 장바구니에 아이템 추가
            CartItem cartItem = marketCartService.insertCartItem(cartRequestDTO);
            log.info("인설트 됨ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");

            resp.put("status", 200);
            return ResponseEntity.ok(resp); // 성공적으로 추가됨
        } catch (Exception e) {
            log.error("장바구니 추가 중 오류 발생: {}", e.getMessage());
            resp.put("status", 500);
            resp.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<Map<String, Object>> updateCart(
            @PathVariable Long cartItemId,
            @RequestBody CartRequestDTO cartRequestDTO) {
        log.info("카트 수량 없데이트 요청됬다!!!!!!!");
        log.info("요청받은 여기가 아이디:"+ cartItemId);
        log.info("요청받은 여기가 수량:"+cartRequestDTO.getQuantity());
        try {
            marketCartService.updateQuantity(cartRequestDTO.getCartItemId(), cartRequestDTO.getQuantity());

            // 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "장바구니 수량이 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 에러 응답
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteCartItem(@RequestBody List<Long> cartItemIds) {
        log.info("삭제 요청 들어왓따ㅣ");

        try {
            cartItemService.deleteCartItems(cartItemIds,0); // 서비스 메서드에서 ID 목록을 처리

            // 성공 응답
            Map<String, String> response = new HashMap<>();
            response.put("message", "장바구니 아이템이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 에러 응답
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/api/cart/cartOrder/{userId}")
    public ResponseEntity<Map<String,String>> CartOrder(@PathVariable String userId, @RequestBody CartOrderRequestDTO orderRequestData) {

        log.info("orderRequestData "+ orderRequestData);
        return null;
    }


}

package com.lotteon.service.order;

import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.service.product.MarketCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartItemService {


    private final CartItemRepository cartItemRepository;
    private final MarketCartService marketCartService;
    private final CartRepository cartRepository;

    public boolean deleteCartItems(List<Long> cartItemIds,long cartID){

        int result=0;
        boolean response=false;

        int size= cartItemIds.size();
        long cartId=cartID;
        for (Long cartItemId : cartItemIds) {
            // 카트 아이템을 먼저 조회하여 카트 ID를 저장
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 읍다"));
            // 카트 ID 저장
            cartId = cartItem.getCart().getCartId();

            cartItemRepository.delete(cartItem);
            result++;

            log.info("삭제 완료했다: cartItemId = " + cartItemId);

        }
        cartItemRepository.flush();

        if(size==result){
            long itemCount = cartItemRepository.countByCart_CartId(cartId);
            log.info("남은 아이템수!!!!!!!!!"+itemCount);
            log.info("삭제한 아이템 수: " + result + ", 삭제된 아이템 IDs: " + cartItemIds);

            if (itemCount == 0) {
                cartRepository.deleteById(cartId);
                log.info("카트 삭제 완료: cartId = " + cartId);
            } else {
                Cart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new RuntimeException("상품 아이디가 읍다"));

                marketCartService.updateCartSummary(cart);

            }
            response=true;
        }

        return response;


    }
}

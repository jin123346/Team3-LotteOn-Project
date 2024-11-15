package com.lotteon.repository.cart;

import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024-10-30
    최영진 : int countByCartId(Long cartId);추가 List<CartItem> findByCart(Cart cart); 추가

 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


//    Optional<CartItem> findByCart_CartIdAndProduct_ProductIdAndOptionCombination_CombinationId(Long cartId, Long productId, long combinationId);
    @Query("SELECT c FROM CartItem c WHERE c.cart.cartId = :cartId AND c.product.productId = :productId AND c.productOptionCombination.combinationId = :combinationId")
    Optional<CartItem> findByCart_CartIdAndProduct_ProductIdAndOptionCombination_CombinationId(
            @Param("cartId") Long cartId,
            @Param("productId") Long productId,
            @Param("combinationId") long combinationId
    );

    Optional<CartItem> findByCart_CartIdAndProduct_ProductId(long cartId, long productId);
    long countByCart_CartId(long cartId);
    List<CartItem> findByCart(Cart cart);

}

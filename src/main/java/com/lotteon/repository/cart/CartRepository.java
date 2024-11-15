package com.lotteon.repository.cart;

import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUser_Uid(String uid);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems WHERE c.user = :user")
    Optional<Cart> findByUserWithItems(@Param("user") User user);


}

package com.lotteon.repository.user;

import com.lotteon.entity.User.Delivery;
import com.lotteon.entity.User.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByMemberId(Long memberId); // 특정 회원의 배송지 목록 조회

    Optional<Object> findByIdAndMemberId(Long deliveryId, Long memberId);

//    void setDefaultAddressForMember(String uid, Member member);
}

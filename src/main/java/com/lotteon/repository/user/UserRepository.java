package com.lotteon.repository.user;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    Optional<User> findByUid(String uid);

    boolean existsByUid(String uid);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role IN ('member', 'seller')")
    long countUsersByRole();

    // 어제 가입한 멤버 수를 조회하는 메서드
    @Query("SELECT COUNT(m) FROM Member m JOIN m.user u WHERE u.role = 'member' AND m.regDate BETWEEN :start AND :end")
    long countNewMembersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 어제 가입한 셀러 수를 조회하는 메서드
    @Query("SELECT COUNT(s) FROM Seller s JOIN s.user u WHERE u.role = 'seller' AND s.regDate BETWEEN :start AND :end")
    long countNewSellersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



//    @Query("SELECT new com.lotteon.dto.User.MemberDTO(m.id, m.name, m.gender, m.email, m.hp, m.postcode, m.addr, m.addr2, m.point, m.grade, u.uid, m.regDate) " +
//            "FROM User u JOIN u.member m")
//    List<MemberDTO> findAllMemberDetails();




}

package com.lotteon.repository.user;

import com.lotteon.entity.User.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUser_Uid(String uid);


    String findByUser_memberName(String uid);

    boolean existsByEmail(String email);

    boolean existsByHp(String hp);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.points order by m.point desc")
    List<Member> findAllWithPoints();

    @Query("SELECT m FROM Member m WHERE m.user.uid = :uid")
    Optional<Member> findByUid(@Param("uid") String uid);

    Optional<Member> findByNameAndEmail(String name, String email);
}


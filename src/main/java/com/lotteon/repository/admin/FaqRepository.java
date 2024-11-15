package com.lotteon.repository.admin;


import com.lotteon.entity.Faq;
import com.lotteon.repository.custom.FaqRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer>, FaqRepositoryCustom {
}

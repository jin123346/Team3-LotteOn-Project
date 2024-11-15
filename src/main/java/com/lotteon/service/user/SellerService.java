package com.lotteon.service.user;


/*
    날짜 ; 2024.10.27
    이름 : 하진희
    내용 : seller.user.uid로 조회
 */

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public Optional<User> findSellerByUid(String uid) {

        return userRepository.findByUid(uid); // 아이디로 사용자 검색
    }

    public String findCompanyByuid(String uid) {
        Optional<Seller> opt= sellerRepository.findByUserUid(uid);
        if(opt.isPresent()) {
            return opt.get().getCompany();
        }
        return null;
    }


    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    private final ModelMapper getModelMapper;

    public SellerDTO getSeller(String sellerId) {
        Optional<Seller> opt = sellerRepository.findByUserUid(sellerId);
        SellerDTO sellerDTO = new SellerDTO();
        if (opt.isPresent()) {
            Seller seller = opt.get();
            sellerDTO = getModelMapper.map(seller, SellerDTO.class);
        }

        log.info("sellerrrrrrrrrrrrrrrr:" + sellerDTO);
        return sellerDTO;
    }

    @Transactional
    public void deleteSellersByIds(List<Long> sellerIds) {
        for (Long sellerId : sellerIds) {
            Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
            if (sellerOptional.isPresent()) {
                // Seller가 존재하면 삭제
                sellerRepository.delete(sellerOptional.get());
            } else {
                throw new EntityNotFoundException("일치하는 아이디의 seller가 없습니다.: " + sellerId);
            }
        }
    }

    @Transactional
    public Seller updateSeller(Long id, Seller updatedSeller) {
        Optional<Seller> existingSellerOpt = sellerRepository.findById(id);
        if (existingSellerOpt.isPresent()) {
            Seller existingMember = existingSellerOpt.get();

            return sellerRepository.save(existingMember);
        }
        return null; // 회원이 존재하지 않는 경우
    }

    public Optional<Seller> findById(long id) {
        return sellerRepository.findById(id);  // Optional<Seller> 반환
    }


}

package com.lotteon.service.search;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class SearchService  implements CouponSearchInterface {

    private final CouponRepository couponRepository;
    private final CouponIssuedRepository couponIssuedRepository;
    private Map<String, Function<String, List<CouponDTO>>> searchMethods;
    private Map<String, Function<String, List<CouponIssuedDTO>>> searchIssuedMethods;

    @PostConstruct
    public void init() {
        // 초기화 시점에 searchService 객체가 완전히 준비되었는지 확인
        log.info("init 맵 초기화됨");
        searchMethods = Map.of(
                "couponName", this::searchByCouponName,  // 쿠폰명으로 검색
                "sellerCompany", this::searchBySellerCompany  // 발급자(판매자명)으로 검색
        );
    }
    @PostConstruct
    public void issuedInit() {
        // 초기화 시점에 searchService 객체가 완전히 준비되었는지 확인
        log.info("init 맵 초기화됨");
        searchIssuedMethods = Map.of(
                "couponType", this::issuedSearchByCouponType,  // 쿠폰명으로 검색
                "sellerCompany", this::issuedSearchByCouponCompany,  // 발급자(판매자명)으로 검색
                "sellerName", this::issuedSearchByCouponName  // 발급자(판매자명)으로 검색
        );
    }

    @Override
    public List<CouponDTO> searchByCouponName(String query) {
        log.info("쿠폰이름 으로 검색");
        return couponRepository.findByCouponNameContaining(query)
                .stream()
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDTO> searchBySellerCompany(String query) {
        log.info("회사이름 으로 검색");
        return couponRepository.findBySellerCompanyContaining(query)
                .stream()
                .map(CouponDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponIssuedDTO> issuedSearchByCouponCompany(String query) {
        log.info("회사이름 으로 검색");
        return couponIssuedRepository.findBySellerCompanyContaining(query)
                .stream()
                .map(CouponIssuedDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponIssuedDTO> issuedSearchByCouponType(String query) {
        log.info("쿠폰타입 으로 검색");
        return couponIssuedRepository.findByCouponTypeContaining(query)
                .stream()
                .map(CouponIssuedDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponIssuedDTO> issuedSearchByCouponName(String query) {
        log.info("쿠폰명 으로 검색");
        return couponIssuedRepository.findByCouponNameContaining(query)
                .stream()
                .map(CouponIssuedDTO::new)
                .collect(Collectors.toList());
    }


    public List<CouponDTO> executeSearch(String category, String query) {
        log.info("execute 요청 들어옴");
        Function<String, List<CouponDTO>> searchMethod = searchMethods.get(category);
        if (searchMethod == null) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
        return searchMethod.apply(query);
    }
    public List<CouponIssuedDTO> executeIssuedSearch(String category, String query) {
        log.info("execute 요청 들어옴");
        Function<String, List<CouponIssuedDTO>> searchMethod = searchIssuedMethods.get(category);
        if (searchMethod == null) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
        return searchMethod.apply(query);
    }



}
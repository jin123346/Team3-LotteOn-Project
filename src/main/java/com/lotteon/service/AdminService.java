package com.lotteon.service;


import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.repository.BannerRepository;
import com.lotteon.entity.Banner;
import com.lotteon.repository.admin.AdminQnaRepository;
import com.lotteon.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final AdminQnaRepository adminQnaRepository;

    public Banner insertBanner(BannerDTO bannerDTO){
        Banner banner = bannerRepository.save(modelMapper.map(bannerDTO, Banner.class));
        return banner;
    }
    public List<BannerDTO> selectAllbanner(){
        List<Banner> banners = bannerRepository.findAll();
        List<BannerDTO> bannerDTOs = new ArrayList<>();
        for (Banner banner : banners) {
            BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);
            bannerDTOs.add(bannerDTO);
        }

        return bannerDTOs;
    }


    public int updateBannerStatus(int id,String status){
        Optional<Banner> banner= bannerRepository.findById(id);
        int result=0;
        int savedstatus=0;
        if(banner.isPresent()){
            Banner updatebanner = banner.get();
            if(status.equals("ACTIVE")){
                savedstatus =0;
            }else if(status.equals("INACTIVE")){
                savedstatus =1;
            }
            updatebanner.setStatus(savedstatus);
            bannerRepository.save(updatebanner);
            result=1;
        }
        return result;
    }
    public List<BannerDTO> getActiveBanners() {
        LocalDateTime now = LocalDateTime.now();

        return bannerRepository.findAll().stream()
                .filter(banner -> {
                    // 배너의 날짜 및 시간 값이 null인지 확인
                    if (banner.getBan_sdate() == null || banner.getBan_edate() == null ||
                            banner.getBan_stime() == null || banner.getBan_etime() == null) {
                        return false; // 하나라도 null이면 필터링
                    }

                    // banner.status가 0인지 확인
                    if (banner.getStatus() != 0) {
                        return false; // status가 0이 아니면 필터링
                    }

                    // 배너가 표시될 날짜 범위를 설정
                    LocalDate startDate = LocalDate.parse(banner.getBan_sdate());
                    LocalDate endDate = LocalDate.parse(banner.getBan_edate());

                    // 배너가 표시될 시간 범위를 설정
                    LocalTime startTime = LocalTime.parse(banner.getBan_stime());
                    LocalTime endTime = LocalTime.parse(banner.getBan_etime());

                    // 현재 날짜가 시작 날짜와 종료 날짜 사이에 있는지 확인
                    boolean isDateInRange = !now.toLocalDate().isBefore(startDate) && !now.toLocalDate().isAfter(endDate);

                    // 현재 시간이 지정된 시간대에 있는지 확인
                    boolean isTimeInRange = (now.toLocalTime().isAfter(startTime) || now.toLocalTime().equals(startTime)) &&
                            (now.toLocalTime().isBefore(endTime) || now.toLocalTime().equals(endTime));

                    return isDateInRange && isTimeInRange;
                })
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }


    public void deleteCheck(List<Integer> data){
        for (Integer id : data) {
            bannerRepository.deleteById(id);
        }
    }



    public Page<adminQnaDTO> getQnaPage(String requestURI, String category, Authentication authentication, Pageable pageable) {
        if ("/mypage/qnadetails".contains(requestURI)) {
            // 마이페이지에서 접근한 경우, 현재 사용자 게시물만 조회
            String uid = authentication.getName();
            Page<Adminqna> pages = adminQnaRepository.findByQnaWriter(uid, pageable);
            List<adminQnaDTO> dtoList = new ArrayList<>();
            if(pages.getContent()==null){
                return new PageImpl<>(dtoList,pageable,pages.getTotalElements());
            }
            // Adminqna -> adminQnaDTO 변환
            dtoList = pages.getContent()
                    .stream()
                    .map(adminqna -> {
                        // 기본 변환
                        adminQnaDTO dto = modelMapper.map(adminqna, adminQnaDTO.class);
                        // adminqna.category를 adminQnaDTO에 설정
                        if (adminqna.getCate() != null) {
                            long categoryId= adminqna.getCate().getBoardCateId();
                            dto.setCategoryid(categoryId);  // adminQnaDTO에 categoryDTO 설정
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            // 변환된 DTO 리스트를 새로운 Page 객체로 반환
            return new PageImpl<>(dtoList, pageable, pages.getTotalElements());

        } else if (category != null) {
            // 특정 카테고리 조회
            Page<Adminqna> pages = adminQnaRepository.findByProductId(Long.parseLong(category), pageable);

            // Adminqna -> adminQnaDTO 변환
            List<adminQnaDTO> dtoList = pages.getContent()
                    .stream()
                    .map(adminqna -> modelMapper.map(adminqna, adminQnaDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, pages.getTotalElements());

        } else {
            // 전체 조회
            Page<Adminqna> pages = adminQnaRepository.findAll(pageable);

            // Adminqna -> adminQnaDTO 변환
            List<adminQnaDTO> dtoList = pages.getContent()
                    .stream()
                    .map(adminqna -> modelMapper.map(adminqna, adminQnaDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, pages.getTotalElements());
        }
//        if ("/mypage/qnadetails".contains(requestURI)) {
//            // 마이페이지에서 접근한 경우, 현재 사용자 게시물만 조회
//            String uid = authentication.getName();
//            log.info("TOTAL!!!!"+adminQnaRepository.findByQnaWriter(uid, pageable).getContent().toString());
//            Page<Adminqna> pages = adminQnaRepository.findByQnaWriter(uid, pageable);
//            List<Adminqna> adminqnas = pages.getContent();
//            adminqnas.stream().map(adminqna -> modelMapper.map(adminqna,adminQnaDTO.class)).collect(Collectors.toList());
//            return null;
//
//
//        } else if (category != null) {
//            // 특정 카테고리 조회
//            adminQnaRepository.findByProductId(Long.parseLong(category), pageable);
//            return null;
//        } else {
//            // 전체 조회
//            adminQnaRepository.findAll(pageable);
//            return null;
//        }
    }


}

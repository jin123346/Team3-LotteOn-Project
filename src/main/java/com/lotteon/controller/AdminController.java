package com.lotteon.controller;



import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.entity.Banner;
import com.lotteon.entity.Notice;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.*;
import com.lotteon.service.admin.NoticeService;
import com.lotteon.service.admin.QnaService;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {


    private final AdminService adminService;
    private final FileService fileService;
    private final OrderService orderService;
    private final UserService userService;
    private final VisitorCountService visitorCountService;
    private final BoardService boardService;
    private final NoticeService noticeService;
    private final QnaService qnaService;
    private final OrderItemRepository orderItemRepository;


    @GetMapping("/main")
    public String adminMain(Model model, @AuthenticationPrincipal MyUserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime yesterdayTime = currentTime.minusHours(24);


        Page<Notice> noticePage = noticeService.getNoticesTop5();
        log.info("noticePage!!!!"+noticePage.getContent());
        model.addAttribute("notices", noticePage.getContent());


        Pageable pageable = PageRequest.of(0,5, Sort.by("date").descending());


        if (authorities.contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
            String sellerUid = userDetails.getUser().getUid();

            Long yesterdaySalesCount = orderService.getSalesCountBySellerAndDateRange(sellerUid, yesterdayTime.minusHours(24), yesterdayTime);
            Long todaySalesCount = orderService.getSalesCountBySellerAndDateRange(sellerUid, yesterdayTime, currentTime);

            System.out.println("어제: " + yesterdaySalesCount);
            System.out.println("오늘: " + todaySalesCount);

            Long yesterdayTotalSalesAmount = orderService.getTotalSalesAmountBySellerAndDateRange(sellerUid, yesterdayTime.minusHours(24), yesterdayTime);
            Long todayTotalSalesAmount = orderService.getTotalSalesAmountBySellerAndDateRange(sellerUid, yesterdayTime, currentTime);

            System.out.println("어제판매량: " + yesterdayTotalSalesAmount);
            System.out.println("오늘판매량: " + todayTotalSalesAmount);

            // null이거나 음수일 경우 0으로 설정
            yesterdaySalesCount = (yesterdaySalesCount == null || yesterdaySalesCount < 0) ? 0 : yesterdaySalesCount;
            todaySalesCount = (todaySalesCount == null || todaySalesCount < 0) ? 0 : todaySalesCount;

            yesterdayTotalSalesAmount = (yesterdayTotalSalesAmount == null || yesterdayTotalSalesAmount < 0) ? 0 : yesterdayTotalSalesAmount;
            todayTotalSalesAmount = (todayTotalSalesAmount == null || todayTotalSalesAmount < 0) ? 0 : todayTotalSalesAmount;

            Long salesCount = orderService.getSalesCountBySeller(sellerUid);
            Long totalSalesAmount = orderService.getTotalSalesAmountBySeller(sellerUid);


            System.out.println("총 판매건수: " + salesCount);
            System.out.println("총 판매금액: " + totalSalesAmount);

            salesCount = (salesCount == null || salesCount < 0) ? 0 : salesCount;
            totalSalesAmount = (totalSalesAmount == null || totalSalesAmount < 0) ? 0 : totalSalesAmount;


            String formattedSalesAmount = String.format("%,d", totalSalesAmount);

            model.addAttribute("salesCount", salesCount);
            model.addAttribute("totalSalesAmount", formattedSalesAmount);

            String formattedYesterdayTotalSalesAmount = String.format("%,d", yesterdayTotalSalesAmount);
            String formattedTodayTotalSalesAmount = String.format("%,d", todayTotalSalesAmount);

            //고객문의
            PageRequestDTO pageRequestDTO = new PageRequestDTO();
            List<adminQnaDTO> qnadto = qnaService.selectQnaListBySeller(pageRequestDTO,sellerUid);
            model.addAttribute("qnadto", qnadto);







            model.addAttribute("yesterdayNewUserCount", "?");
            model.addAttribute("todayNewUserCount", "?");

            long visitorCount = visitorCountService.getVisitorCount(); // 방문자 수 가져오기
            model.addAttribute("visitorCount", visitorCount);

            long yesterdayVisitorCount = visitorCountService.getYesterdayVisitorCount(); // 어제 방문자 수
            long todayVisitorCount = visitorCountService.getTodayVisitorCount(); // 오늘 방문자 수

            long readyForShippingCount = orderItemRepository.countReadyForShippingBySellerUid(sellerUid, DeliveryStatus.PREPARING);
            Long exchangeForShippingCount = orderItemRepository.countReadyForShippingBySellerUid(sellerUid, DeliveryStatus.EXCHANGE_REQUESTED);
            exchangeForShippingCount = (exchangeForShippingCount != null) ? exchangeForShippingCount : 0;

            Long returnForShippingCount = orderItemRepository.countReadyForShippingBySellerUid(sellerUid, DeliveryStatus.RETURN_REQUESTED);
            returnForShippingCount = (returnForShippingCount != null) ? returnForShippingCount : 0;

            long waitingCount = orderService.getWaitingOrderCount();
            model.addAttribute("waitingCount", waitingCount);


            model.addAttribute("readyForShippingCount", readyForShippingCount);
            model.addAttribute("exchangeForShippingCount",  exchangeForShippingCount);
            model.addAttribute("returnForShippingCount",  returnForShippingCount);

            model.addAttribute("yesterdayVisitorCount", yesterdayVisitorCount);
            model.addAttribute("todayVisitorCount", todayVisitorCount);

            model.addAttribute("yesterdaySalesCount", yesterdaySalesCount);
            model.addAttribute("todaySalesCount", todaySalesCount);
            model.addAttribute("yesterdayTotalSalesAmount", formattedYesterdayTotalSalesAmount);
            model.addAttribute("todayTotalSalesAmount", formattedTodayTotalSalesAmount);

            // 회원가입 수는 '?'로 표시
            model.addAttribute("totalUserCount", "?");

        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            long totalSalesCount = orderService.getTotalSalesCountForAllSellers();
            long totalSalesAmountForAll = orderService.getTotalSalesAmountForAllSellers();

            long yesterdayTotalSalesCount = orderService.getTotalSalesCountForAllSellersByDateRange(yesterdayTime.minusHours(24), yesterdayTime);
            long todayTotalSalesCount = orderService.getTotalSalesCountForAllSellersByDateRange(yesterdayTime, currentTime);

            long yesterdayTotalSalesAmountForAll = orderService.getTotalSalesAmountForAllSellersByDateRange(yesterdayTime.minusHours(24), yesterdayTime);
            long todayTotalSalesAmountForAll = orderService.getTotalSalesAmountForAllSellersByDateRange(yesterdayTime, currentTime);

            String formattedYesterdayTotalSalesAmount = String.format("%,d", yesterdayTotalSalesAmountForAll);
            String formattedTodayTotalSalesAmount = String.format("%,d", todayTotalSalesAmountForAll);

            String formattedTotalSalesAmount = String.format("%,d", totalSalesAmountForAll);

            long totalUserCount = userService.getTotalUserCount();

            PageRequestDTO pageRequestDTO= PageRequestDTO.builder()
                    .size(5)
                    .build();
            QnaPageResponseDTO responseDTO = qnaService.selectQnaListAll(pageRequestDTO);
            List<adminQnaDTO> adminQnaDTOS = responseDTO.getQnadtoList();
            log.info("adminQnaDTOS"+adminQnaDTOS);
            model.addAttribute("qnadto", adminQnaDTOS);


            long yesterdayNewUserCount = userService.getYesterdayNewUserCount();
            long todayNewUserCount = userService.getTodayNewUserCount();
            long visitorCount = visitorCountService.getVisitorCount(); // 방문자 수 가져오기

            long waitingCount = orderService.getWaitingOrderCount();
            model.addAttribute("waitingCount", waitingCount);

            model.addAttribute("visitorCount", visitorCount);
            model.addAttribute("yesterdayNewUserCount", yesterdayNewUserCount);
            model.addAttribute("todayNewUserCount", todayNewUserCount);

//            PageRequestDTO pageRequestDTO = new PageRequestDTO();
//            Pageable pageable = PageRequest.of(0,5, Sort.by("date").descending());

//            QnaPageResponseDTO qnadto = qnaService.selectQnaListAll(pageRequestDTO);


            long yesterdayVisitorCount = visitorCountService.getYesterdayVisitorCount(); // 어제 방문자 수
            long todayVisitorCount = visitorCountService.getTodayVisitorCount(); // 오늘 방문자 수

            DeliveryStatus readyForShippingStatus = DeliveryStatus.PREPARING;
            long allReadyForShippingCount = orderItemRepository.countAllReadyForShipping(readyForShippingStatus);

            DeliveryStatus ExchangeForShippingStatus = DeliveryStatus.EXCHANGE_REQUESTED;
            Long allExchangeForShippingCount = orderItemRepository.countAllReadyForShipping(ExchangeForShippingStatus);
            allExchangeForShippingCount = (allExchangeForShippingCount != null) ? allExchangeForShippingCount : 0;

            DeliveryStatus ReturnForShippingStatus = DeliveryStatus.RETURN_REQUESTED;
            Long allReturnForShippingCount = orderItemRepository.countAllReadyForShipping(ReturnForShippingStatus);
            allReturnForShippingCount = (allReturnForShippingCount != null) ? allReturnForShippingCount : 0;


            model.addAttribute("readyForShippingCount", allReadyForShippingCount);
            model.addAttribute("exchangeForShippingCount", allExchangeForShippingCount);
            model.addAttribute("returnForShippingCount", allReturnForShippingCount);



            model.addAttribute("yesterdayVisitorCount", yesterdayVisitorCount);
            model.addAttribute("todayVisitorCount", todayVisitorCount);



            model.addAttribute("salesCount", totalSalesCount);
            model.addAttribute("totalSalesAmount", formattedTotalSalesAmount);
            model.addAttribute("yesterdaySalesCount", yesterdayTotalSalesCount);
            model.addAttribute("todaySalesCount", todayTotalSalesCount);
            model.addAttribute("yesterdayTotalSalesAmount", formattedYesterdayTotalSalesAmount);
            model.addAttribute("todayTotalSalesAmount", formattedTodayTotalSalesAmount);
            model.addAttribute("totalUserCount", totalUserCount); // 회원가입 수 추가


        } else {
            return "error/unauthorized";
        }

        return "content/admin/admin_index";
    }

    @ResponseBody
    @DeleteMapping("/banner/delete")
    public ResponseEntity<?> bannerDelete(@RequestBody List<Integer> data){
         if(data == null || data.isEmpty()){
             return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
         }
         adminService.deleteCheck(data);
         return ResponseEntity.ok().build();
    }

    @GetMapping("/config/banner")
    public String bannerList(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        model.addAttribute("banners", banners);
        return "content/admin/config/admin_Banner";
    }

    @ResponseBody
    @PostMapping("/banner/upload")
    public ResponseEntity<?> banner(@ModelAttribute BannerDTO bannerDTO , Model model) {
        log.info("bannerDTO :" + bannerDTO);


        //파일 업로드
        BannerDTO newBanner = fileService.uploadFile(bannerDTO);

        bannerDTO.setBan_oname(newBanner.getBan_oname()); // 원래 파일 이름 설정
        bannerDTO.setBan_image(newBanner.getBan_image()); // 저장된 파일 이름 설정

        //글저장
        Banner banner = adminService.insertBanner(bannerDTO);

        return ResponseEntity.ok().body(banner);
    }



}

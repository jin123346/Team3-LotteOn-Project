package com.lotteon.controller;
import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.admin.AdminOrderDTO;
import com.lotteon.dto.admin.BannerStatusRequest;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.page.AdminOrderPageResponseDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.service.AdminService;
import com.lotteon.service.BoardService;
import com.lotteon.service.admin.AdminOrderService;
import com.lotteon.service.admin.FaqService;
import com.lotteon.service.admin.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardAPIController {


    private final QnaService qnaService;
    private final FaqService faqService;
    private final BoardService boardService;
    private final AdminOrderService adminOrderService;
    private final AdminService adminService;

    @ResponseBody
    @GetMapping("/qna/list/page")
    public ResponseEntity<?> adminQnaListPage(@RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, @RequestParam(required = false) String qnawriter, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setParentId(parentId);
        pageRequestDTO.setChildId(childId);
        pageRequestDTO.setQnawriter(qnawriter);

        QnaPageResponseDTO qnaPageResponseDTO = qnaService.selectQnaListAll(pageRequestDTO);

        // qnadtoList에서 writer 아이디 마스킹
        for (adminQnaDTO qnaDTO : qnaPageResponseDTO.getQnadtoList()) {
            if (qnaDTO.getQnawriter() != null) {
                qnaDTO.setQnawriter(maskUsername(qnaDTO.getQnawriter())); // writer 아이디 마스킹 처리
            }
        }
        return ResponseEntity.ok(qnaPageResponseDTO);

    }

    // 아이디 마스킹 메소드
    public String maskUsername(String username) {
        if (username.length() <= 3) {
            return username; // 아이디가 3자 이하일 경우 그대로 반환
        }
        // 앞의 3자는 그대로 두고 나머지는 마스킹 처리
        return username.substring(0, 3) + "****";
    }

    @ResponseBody
    @GetMapping("/faq/list/page")
    public ResponseEntity<?> adminFaqListPage( @RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setParentId(parentId);
        log.info("이거먼데!!!!" + pageRequestDTO);
        pageRequestDTO.setChildId(childId);
        log.info("이건또먼데!!!!!" + pageRequestDTO);

        FaqPageResponseDTO faqPageResponseDTO = faqService.selectfaqListAll(pageRequestDTO);
        log.info("asdfasdf!!!! : " + faqPageResponseDTO);

        return ResponseEntity.ok(faqPageResponseDTO);
    }


    @GetMapping("/faq/subcate/{parentId}")
    @ResponseBody
    public List<BoardCateDTO> adminFaqOption(@PathVariable Long parentId){
        List<BoardCateDTO> boardsubCate = boardService.selectBoardSubCate(parentId);
        return boardsubCate;
    }


    @ResponseBody
    @GetMapping("/order/status/page")
    public ResponseEntity<?> orderStatusKeyword(@RequestParam(required = false) String type, @RequestParam(required = false) String keyword, com.lotteon.dto.page.PageRequestDTO pageRequestDTO){
        log.info("ckckckckckck : " + type);
        log.info("cjcjcjcjcjcjcj : " + keyword);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);
       // AdminOrderPageResponseDTO adminOrderPageResponseDTO = adminOrderService.selectOrderListAll(pageRequestDTO);
        AdminOrderItemPageResponseDTO adminOrderItemPageResponseDTO = adminOrderService.selectOrderItemListAll(pageRequestDTO);
        log.info("qiqiqiqiqiqi: " + adminOrderItemPageResponseDTO);
        return ResponseEntity.ok(adminOrderItemPageResponseDTO);
    }
    @ResponseBody
    @GetMapping("/order/status/modal")
    public ResponseEntity<?> orderStatusKeyword(@RequestParam(required = false) long id){
        AdminOrderDTO adminOrderDTO = adminOrderService.selectOrderAll(id);
        log.info("pqpqpqpqpqpqpqpq:" + adminOrderDTO);
        return ResponseEntity.ok(adminOrderDTO);
    }


        @ResponseBody
    @GetMapping("/admin/banner/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam int id, @RequestParam String status) {
        HashMap<String,String> response = new HashMap<>();
        try {

            int result=adminService.updateBannerStatus(id, status);
            if(result>0){
                response.put("status","success");

            }else{
                response.put("status","fail");
            }
            log.info("response"+response);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status","fail");
            return ResponseEntity.ok(response);
        }

    }





}
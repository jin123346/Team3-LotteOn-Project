package com.lotteon.controller;


import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.FaqDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.entity.Faq;
import com.lotteon.service.BoardService;
import com.lotteon.service.admin.FaqService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/faq")
public class AdminFaqController {

    private final FaqService faqService;
    private final BoardService boardService;

    @GetMapping("/list")
    public String adminFaqList( Model model, PageRequestDTO pageRequestDTO) {


        FaqPageResponseDTO faqPageResponseDTO = faqService.selectfaqListAll(pageRequestDTO);
        model.addAttribute(faqPageResponseDTO);
        log.info("list faq"+faqPageResponseDTO);


        List<BoardCateDTO> boardCateDTOS = boardService.selectBoardCate();
        log.info(boardCateDTOS);
        model.addAttribute("boardCate",boardCateDTOS);

        return "content/admin/faq/faqList";
    }
//    @ResponseBody
//    @GetMapping("/list/page")
//    public ResponseEntity<?> adminFaqListPage( @RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, PageRequestDTO pageRequestDTO) {
//        pageRequestDTO.setParentId(parentId);
//        log.info("이거먼데!!!!" + pageRequestDTO);
//        pageRequestDTO.setChildId(childId);
//        log.info("이건또먼데!!!!!" + pageRequestDTO);
//
//        FaqPageResponseDTO faqPageResponseDTO = faqService.selectfaqListAll(pageRequestDTO);
//        log.info("asdfasdf!!!! : " + faqPageResponseDTO);
//
//        return ResponseEntity.ok(faqPageResponseDTO);
//    }
//
//
//    @GetMapping("/subcate/{parentId}")
//    @ResponseBody
//    public List<BoardCateDTO> adminFaqOption(@PathVariable Long parentId){
//        List<BoardCateDTO> boardsubCate = boardService.selectBoardSubCate(parentId);
//        return boardsubCate;
//    }

    @GetMapping("/modify")
    public String adminFaqModify(Model model, int no) {
        FaqDTO faqDTO = faqService.selectfaq(no);
        model.addAttribute("faq", faqDTO);

        List<BoardCateDTO> boardCateDTOS = boardService.selectBoardCate();
        log.info(boardCateDTOS);
        model.addAttribute("boardCate",boardCateDTOS);
        return "content/admin/faq/faqModify";
    }

    @ResponseBody
    @PostMapping("/modify")
    public ResponseEntity<?> adminFaqModify(@ModelAttribute FaqDTO faqDTO) {
        log.info("asdfadfasfdasdfasdfsadfasdfsadfsdafasdfdsafsdafasdfsd" + faqDTO);
        Faq faq = faqService.updatefaq(faqDTO);
        log.info("faqasdfasdfasdf : " + faq.toString());
        return ResponseEntity.ok().body(faq);
    }

    @GetMapping("/view")
    public String adminFaqView(int no, Model model) {
        FaqDTO faqDTO = faqService.selectfaq(no);
        model.addAttribute("faq", faqDTO);
        return "content/admin/faq/faqView";
    }

    @GetMapping("/write")
    public String adminFaqWrite(Model model) {

        List<BoardCateDTO> boardCateDTOS = boardService.selectBoardCate();
        log.info(boardCateDTOS);
        model.addAttribute("boardCate",boardCateDTOS);
        return "content/admin/faq/faqWrite";
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> adminFaqWrite1(Model model, @ModelAttribute FaqDTO faqDTO) {
        log.info("FaQDTO >L: >>>>>>:"+faqDTO);
        Faq faq = faqService.insertfaq(faqDTO);
        return ResponseEntity.ok().body(faq);
    }

    @ResponseBody
    @DeleteMapping("/delete/check")
    public ResponseEntity<?> adminFaqDeleteCheck(@RequestBody List<Integer> data){
        if(data == null || data.isEmpty()){
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }
        faqService.deleteCheck(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete")
    public String adminFaqDelete(int no , RedirectAttributes redirectAttributes){
        faqService.deletefaq(no);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다."); // 메시지 추가
        return "redirect:/admin/faq/list";

    }
}

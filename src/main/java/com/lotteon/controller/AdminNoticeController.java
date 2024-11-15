package com.lotteon.controller;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.NoticePageResponseDTO;
import com.lotteon.entity.Notice;
import com.lotteon.entity.NoticeType;
import com.lotteon.service.admin.NoticeService;
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
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final NoticeService noticeService;


    @GetMapping("/list")
    public String adminNoticeList(Model model , PageRequestDTO pageRequestDTO) {
        NoticePageResponseDTO noticePageResponseDTO = noticeService.selectNoticeListAll(pageRequestDTO);
        log.info("왜왜왜왜오애왜왜오애오왕애오애왱ㅇ :" + noticePageResponseDTO);
        model.addAttribute(noticePageResponseDTO);

        model.addAttribute("noticeTypes", NoticeType.values());
        return "content/admin/notice/noticeList";
    }

    @ResponseBody
    @GetMapping("/list/check")
    public ResponseEntity<?> adminNoticeListCheck(Model model , PageRequestDTO pageRequestDTO) {
        NoticePageResponseDTO noticePageResponseDTO = noticeService.selectNoticeListAll(pageRequestDTO);
        log.info("흐어어어어어어어어엉엉흡엌어어어엉엉 :" + noticePageResponseDTO);
        model.addAttribute(noticePageResponseDTO);
        model.addAttribute("noticeTypes", NoticeType.values());
        return ResponseEntity.ok(noticePageResponseDTO);
    }

    @GetMapping("/modify")
    public String adminNoticeModify(Model model, Long no) {
        NoticeDTO noticeDTO = noticeService.selectNotice(no);
        model.addAttribute("notice", noticeDTO);
        model.addAttribute("noticeTypes", NoticeType.values());
        return "content/admin/notice/noticeModify";
    }

    @ResponseBody
    @PostMapping("/modify")
    public ResponseEntity<?> adminNoticeModify(Model model, NoticeDTO noticeDTO) {
        log.info("adminNoticeModify :" + noticeDTO);
        Notice notice = noticeService.UpdateNotice(noticeDTO);
        return ResponseEntity.ok().body(notice);
    }

    @GetMapping("/view")
    public String adminNoticeView(Model model, Long no) {
        NoticeDTO noticeDTO = noticeService.selectNotice(no);
        model.addAttribute("notice", noticeDTO);
        return "content/admin/notice/noticeView";
    }

    @GetMapping("/write")
    public String adminNoticeWrite(Model model) {
        model.addAttribute("noticeTypes", NoticeType.values());
        return "content/admin/notice/noticeWrite";
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> adminNoticeWrite(@ModelAttribute NoticeDTO noticeDTO) {
        Notice notice = noticeService.insertNotice(noticeDTO);
        return ResponseEntity.ok().body(notice);
    }

    @ResponseBody
    @DeleteMapping("/delete/check")
    public ResponseEntity<?> adminNoticeDeleteCheck(@RequestBody List<Long> data) {
            if(data == null || data.isEmpty()){
                return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
            }
            noticeService.deleteCheck(data);
            return ResponseEntity.ok().build();
        }
    @GetMapping("/delete")
    public String adminFaqDelete(Long no , RedirectAttributes redirectAttributes){
        noticeService.deleteNotice(no);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다."); // 메시지 추가
        return "redirect:/admin/notice/list";

    }


}



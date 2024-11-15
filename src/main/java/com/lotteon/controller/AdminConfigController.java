package com.lotteon.controller;


import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.dto.admin.BannerStatusRequest;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.dto.admin.TermsDto;
import com.lotteon.entity.FooterInfo;
import com.lotteon.entity.HeaderInfo;
import com.lotteon.entity.admin.Terms;
import com.lotteon.repository.FooterInfoRepository;
import com.lotteon.repository.HeaderInfoRepository;
import com.lotteon.service.*;
import com.lotteon.service.admin.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {

    private final AdminService adminService;
    private final FileService fileService;
    private final FooterInfoService footerInfoService;
    private final FooterInfoRepository footerInfoRepository;
    private final HeaderInfoService headerInfoService;
    private final HeaderInfoRepository headerInfoRepository;
    private final VersionService versionService;
    private final TermsService termsService;

//    @GetMapping("/banner")
//    public String adminBanner(Model model) {
//        model.addAttribute("cate", "config");
//        model.addAttribute("content", "banner");
//        return "content/admin/config/admin_Banner";
//    }

    @GetMapping("/terms")
    public String adminTerms(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "terms");

        List<Terms> termsList = termsService.findAllTerms();
        model.addAttribute("termsList", termsList);

        return "content/admin/config/admin_Terms";
    }
    @PostMapping("/terms")
    public String updateTerms(@ModelAttribute TermsDto termsDto, Model model) {
        // 약관 수정
        termsService.updateTermsContent(termsDto);
        // 사용자에게 수정 완료 메시지 추가
        model.addAttribute("message", "약관이 수정되었습니다.");
        // 약관 목록을 다시 가져와서 모델에 추가
        model.addAttribute("termsList", termsService.findAllTerms());
        // 약관 관리 페이지로 리다이렉트
        return "content/admin/config/admin_Terms";
    }




    @GetMapping("/version")
    public String adminVersion(Model model) {
        List<VersionDTO> versionList = versionService.getAllVersions();
        model.addAttribute("versionList", versionList);  // 데이터를 모델에 추가
        return "content/admin/config/admin_Version";
    }

    @ResponseBody
    @DeleteMapping("/version/delete")
    public ResponseEntity<?> bannerDelete(@RequestBody List<Integer> data){
        if(data == null || data.isEmpty()){
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }
        versionService.deleteCheck(data);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/version")
    public ResponseEntity<String> insertVersion(Model model, @RequestBody VersionDTO versionDTO) {
        versionService.insertVersion(versionDTO);
        return ResponseEntity.ok("{\"message\":\"버전이 등록되었습니다.\"}"); // JSON 응답으로 변경
    }

    @GetMapping("/basic")
    public String adminBasic(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "basic");

        FooterInfoDTO footerInfo = footerInfoService.getFooterInfo();
        HeaderInfoDTO headerInfo = headerInfoService.getHeaderInfo();

        // 조회한 정보를 모델에 추가
        model.addAttribute("headerInfo", headerInfo);
        model.addAttribute("footerInfo", footerInfo);
        return "content/admin/config/admin_basic";
    }


    @PostMapping("/footerInfo")
    public ResponseEntity<Void> saveFooterInfo(@RequestBody FooterInfoDTO footerInfo) {

        System.out.println("Received ID: " + footerInfo.getFt_id());
        if (footerInfo.getFt_id() != null) { // ID가 존재하면 업데이트 로직
            boolean exists = footerInfoService.existsById(footerInfo.getFt_id());
            if (exists) {
                // 데이터가 존재하면 업데이트
                footerInfoService.updateFooterInfo(footerInfo);
            } else {
                // ID가 있지만 데이터베이스에 존재하지 않으면 삽입
                footerInfoService.saveFooterInfo(footerInfo);
            }
        } else {
            // ID가 없으면 새로운 데이터로 간주하고 삽입
            footerInfoService.saveFooterInfo(footerInfo);
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/headerInfo")
    public ResponseEntity<?> saveHeaderInfo(@ModelAttribute HeaderInfoDTO headerInfoDTO) {

        if (headerInfoDTO.getHd_id() != null) {
            boolean exists = headerInfoService.existsById(headerInfoDTO.getHd_id());
            if (exists) {
                // 데이터가 존재하면 업데이트
                headerInfoService.updateHeaderInfo(headerInfoDTO);
            } else {
                // ID가 있지만 데이터베이스에 존재하지 않으면 삽입
                headerInfoService.saveHeaderInfo(headerInfoDTO);
            }
        } else {
            // ID가 없으면 새로운 데이터로 간주하고 삽입
            headerInfoService.saveHeaderInfo(headerInfoDTO);
        }

        return ResponseEntity.ok().build();
    }


    @PostMapping("/headerLogoInfo")
    public ResponseEntity<?> saveHeaderLogoInfo(@ModelAttribute HeaderInfoDTO headerInfoDTO) {
        // 파일 업로드 수행
        HeaderInfoDTO newheaderInfo = fileService.uploadFiles(headerInfoDTO);

        headerInfoDTO.setHd_sName1(newheaderInfo.getHd_sName1());
        headerInfoDTO.setHd_sName2(newheaderInfo.getHd_sName2());
        headerInfoDTO.setHd_sName3(newheaderInfo.getHd_sName3());

        headerInfoDTO.setHd_oName1(newheaderInfo.getHd_oName1());
        headerInfoDTO.setHd_oName2(newheaderInfo.getHd_oName2());
        headerInfoDTO.setHd_oName3(newheaderInfo.getHd_oName3());

        if (headerInfoDTO.getHd_id() != null) {
            boolean exists = headerInfoService.existsById(headerInfoDTO.getHd_id());
            if (exists) {
                // 데이터가 존재하면 업데이트
                headerInfoService.updateHeaderInfo2(headerInfoDTO);
            } else {
                // ID가 있지만 데이터베이스에 존재하지 않으면 삽입
                headerInfoService.saveHeaderInfo2(headerInfoDTO);
            }
        } else {
            // ID가 없으면 새로운 데이터로 간주하고 삽입
            headerInfoService.saveHeaderInfo2(headerInfoDTO);
        }

        return ResponseEntity.ok().build();
    }




}

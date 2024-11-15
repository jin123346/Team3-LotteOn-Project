package com.lotteon.controller;

import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.service.AdminService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MainController {


    private final AdminService adminService;
    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model){

        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        List<ProductDTO> hitProduct =  productService.selectMainList(0,"hit");
        log.info("hitProduct!!!:"+hitProduct);
        List<ProductDTO> soldProduct =  productService.selectMainList(0,"sold");

        log.info("soldProduct!!!:"+soldProduct);

        List<ProductDTO> rDateProduct =  productService.selectMainList(0,"rdate");
        List<ProductDTO> discountProduct =  productService.selectMainList(0,"discount");
        List<ProductDTO> ratingProduct =  productService.selectMainList(0,"rating");

        model.addAttribute("hitProduct",hitProduct);
        model.addAttribute("soldProduct",soldProduct);
        model.addAttribute("rDateProduct",rDateProduct);
        model.addAttribute("discountProduct",discountProduct);
        model.addAttribute("ratingProduct",ratingProduct);
        log.info("gdgd :" + banners);
        model.addAttribute("banners", banners2);
        return "mainIndex";
    }


    @GetMapping("/category")
    public String category(){
        return "category";
    }



}

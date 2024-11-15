package com.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/home")
    public String companyHome(Model model) {
        model.addAttribute("content", "home");
        return "content/company/index"; // Points to the "content/company/index" template
    }


    @GetMapping("/culture")
    public String companyCulture(Model model) {
        model.addAttribute("content", "culture");
        return "content/company/culture"; // Points to the "content/company/culture" template
    }

    @GetMapping("/story")
    public String companyStory(Model model) {
        model.addAttribute("content", "story");
        return "content/company/story"; // Points to the "content/company/story" template
    }

    @GetMapping("/media")
    public String companyMedia(Model model) {
        model.addAttribute("content", "media");
        return "content/company/media"; // Points to the "content/company/media" template
    }

    @GetMapping("/recruit")
    public String companyRecruit(Model model) {
        model.addAttribute("content", "recruit");
        return "content/company/recruit"; // Points to the "content/company/recruit" template
    }

}

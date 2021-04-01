package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GcoachController {

    private final JoinService joinService;

    @GetMapping("/test")
    public void testPage(){
        log.info("testPage..........");
    }

    @GetMapping("/login")
    public void login() {
        log.info("login..........");
    }

    @GetMapping("/join")
    public void join() {
        log.info("join..........");
    }

    @PostMapping("/join")
    public String join(MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        log.info("----------회원가입----------");
        log.info("회원정보 : " + memberDTO);
        String result = joinService.join(memberDTO);
        if (result.equals("fail")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 실패");
            return "redirect:/login";
        } else if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 성공");
            return "redirect:/login";
        }
        return null;
    }
}

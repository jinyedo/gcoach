package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

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
    public String join(MemberDTO memberDTO) {
        log.info("join..........");
        return "/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("----------회원가입----------");
        log.info("회원정보 : " + memberDTO);
        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("memberDTO", memberDTO);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = joinService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/join";
        }

        String result = joinService.join(memberDTO);
        if (result.equals("fail")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 실패");
            return "redirect:/login";
        } else if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 성공");
            return "redirect:/login";
        }
        return "redirect:/login";
    }
}

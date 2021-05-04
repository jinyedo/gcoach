package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/profile_main")
    public void getProfile(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("getProfile_main..........");
        MemberDTO memberDTO = entityToDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
    }

    @GetMapping("/profile_changeNickname")
    public void getChangeNickname(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("getProfile_changeNickname..........");
        MemberDTO memberDTO = entityToDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
    }

    @PostMapping("/profile_changeNickname")
    public String postChangeNickname(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                                     @Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("postProfile_changeNickname..........");
        MemberDTO memberDTO2 = entityToDto(authMemberDTO);
        boolean changeNickname = true;

        log.info("기존 닉네임 : " + memberDTO2.getNickname());
        log.info("변경할 닉네임 : " + memberDTO.getNickname());

        if (errors.hasErrors()) {
            log.info("-----닉네임 변경 유효성 검사 오류 확인-----");
            for (FieldError error : errors.getFieldErrors()) {
                if (error.getField().equals("nickname")) {
                    log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
                    changeNickname = false;
                }
            }
            log.info("----------------------------");
        }
        log.info(changeNickname);
        if (!changeNickname) {
            model.addAttribute("msg", "변경 실패");
            return "/profile_changeNickname";
        } else {
            memberRepository.updateNickname(memberDTO.getNickname(), memberDTO2.getUsername());
            model.addAttribute("msg", "닉네임 변경이 완료되었습니다.");
            return "/profile_main";
        }
    }

    @RequestMapping("/checkNickname")
    @ResponseBody
    public String postCheckNickname(String nickname) {
        log.info("닉네임 : " + nickname);
        String result = memberService.checkNickname(nickname);
        log.info("닉네임 중복 여부 : " + result);
        return result;
    }

    private MemberDTO entityToDto(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        return MemberDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .build();
    }
}

package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.dto.ReplyDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.JoinService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GcoachController {

    private final MemberRepository memberRepository;
    private final JoinService joinService;

    @GetMapping("/test")
    public void testPage() {
        log.info("testPage..........");
    }

    @GetMapping("/login")
    public void getLogin() {
        log.info("getLogin..........");
    }

    @PostMapping("/login")
    public void postLogin() {
        log.info("postLogin..........");
    }

    @RequestMapping("/checkUsername")
    @ResponseBody
    public String checkUsername(MemberDTO memberDTO) {
        log.info("checkUsername : " + memberDTO.getUsername());
        String result = joinService.checkUsername(memberDTO);
        log.info(result);
        return result;
    }

    @GetMapping("/join")
    public String getJoin(MemberDTO memberDTO) {
        log.info("getJoin..........");
        return "/join";
    }

    @PostMapping("/join")
    public String postJoin(@Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("postJoin..........");
        log.info("회원정보 : " + memberDTO);

        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("memberDTO", memberDTO);

            log.info("-----유효성 검사 오류 종류-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------");

            return "/join";
        }

        String result = joinService.join(memberDTO);
        if (result.equals("fail")) {
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("msg", memberDTO.getUsername() + "은 이미 존재하는 아이디입니다.");
            return "/join";
        } else if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 성공");
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    @GetMapping("/play")
    public void getPlay(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) throws JsonProcessingException {
        log.info("getPlay..........");
        Optional<Member> result = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (result.isPresent()) {
            Member member = result.get();
            PlayDTO playDTO = PlayDTO.builder()
                    .mid(member.getMid())
                    .nickname(member.getNickname())
                    .cid(1L)
                    .contentType("asmr")
                    .contentName("hometown.mp3")
                    .likeCount(0)
                    .build();
            log.info(playDTO);
            model.addAttribute("dto", playDTO);
        }
    }

    public MemberDTO authMemberDtoToMemberDto(AuthMemberDTO authMemberDTO) {
        Optional<Member> result = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (result.isPresent()) {
            Member member = result.get();
            return MemberDTO.builder()
                    .mid(member.getMid())
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .phone(member.getPhone())
                    .formSocial(member.isFormSocial())
                    .socialType(member.getSocialType())
                    .emotion(member.getEmotion())
                    .interest(member.getInterest())
                    .build();
        }
        return null;
    }
}
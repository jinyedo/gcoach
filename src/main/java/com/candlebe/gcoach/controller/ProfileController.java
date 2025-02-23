package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.Optional;

/* 프로필 컨트롤러 */
@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ProfileController {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /* 프로필 메인 페이지 */
    @GetMapping("/profile_main")
    public void getProfile(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("getProfile_main..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
    }
    /* ***** */

    /* 닉네임 변경 페이지 */
    @GetMapping("/profile_changeNickname")
    public void getChangeNickname() {
        log.info("getProfile_changeNickname..........");
    }

    @PostMapping("/profile_changeNickname")
    public String postChangeNickname(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                                     @Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("postProfile_changeNickname..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            boolean changeNickname = false;
            log.info("기존 닉네임 : " + member.getNickname());
            log.info("변경할 닉네임 : " + memberDTO.getNickname());

            if (errors.hasErrors()) {
                log.info("-----닉네임 변경 유효성 검사 오류 확인-----");
                for (FieldError error : errors.getFieldErrors()) {
                    if (error.getField().equals("nickname")) {
                        log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
                        changeNickname = true;
                    }
                }
                log.info("----------------------------");
            }
            log.info("오류 여부 : " + changeNickname);
            if (changeNickname) {
                model.addAttribute("msg", "변경 실패");
                return "/profile_changeNickname";
            } else {
                member.setNickname(memberDTO.getNickname());
                memberRepository.save(member);
                MemberDTO memberDTO2 = entityToDto(member);
                model.addAttribute("msg", "닉네임 변경이 완료되었습니다.");
                model.addAttribute("memberDTO", memberDTO2);
                return "profile_main";
            }
        }
        return "redirect:/logout";
    }
    /* ***** */

    /* 비밀번호 변경 페이지 */
    @GetMapping("/profile_changePassword")
    public void getChangePassword() {
        log.info("getProfile_changePassword..........");
    }

    @PostMapping("/profile_changePassword")
    public String postChangePassword(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                                     @Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {

        log.info("postProfile_changePassword..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            log.info("변경할 비밀번호 : " + memberDTO.getPassword());
            boolean checkError = false;
            if (errors.hasErrors()) {
                log.info("-----유효성 검사 오류 종류-----");
                for (FieldError error : errors.getFieldErrors()) {
                    if (error.getField().equals("password") || error.getField().equals("confirmPassword") || error.getField().equals("checkPassword")) {
                        log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
                        checkError = true;
                    }
                }
                log.info("----------------------------");
            }
            log.info("오류 여부 : " + checkError);
            if (checkError) {
                model.addAttribute("msg", "비밀번호 변경 실패");
                return "/profile_changePassword";
            } else {
                member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
                memberRepository.save(member);
                MemberDTO memberDTO2 = entityToDto(member);
                model.addAttribute("msg", "비밀번호 변경이 완료되었습니다.");
                model.addAttribute("memberDTO", memberDTO2);
                return "profile_main";
            }
        }
        return "redirect:/logout";
    }

    @RequestMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, String password) {
        log.info("----------비밀번호 비교 검사----------");
        log.info("비교할 비밀번호 : " + password);
        boolean checkPassword = passwordEncoder.matches(password, authMemberDTO.getPassword());
        log.info("비밀번호 비교 결과 : " + checkPassword);
        log.info("---------------------------------");
        return checkPassword;
    }
    /* ***** */

    /* 관심사 변경 페이지 */
    @GetMapping("/profile_changeInterest")
    public void getChangeInterest() {
        log.info("getChangeInterest..........");
    }

    @PostMapping("/profile_changeInterest")
    public String  postChangeInterest(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, String interest, Model model) {
        log.info("postChangeInterest..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            log.info("기존 회원의 관심사 : " + member.getInterest());
            log.info("변경할 관심사 : " + interest);
            member.setInterest(interest);
            memberRepository.save(member);
            log.info("변경 후 회원의 관심사 : " + member.getInterest());
            MemberDTO memberDTO = entityToDto(member);
            model.addAttribute("msg", "관심사 변경이 완료되었습니다.");
            model.addAttribute("memberDTO", memberDTO);
            return "profile_main";
        }
        return "redirect:/logout";
    }
    /* ***** */

    /* 감정 변경 페이지 */
    @GetMapping("/profile_changeEmotion")
    public void getChangeEmotion() {
        log.info("getChangeEmotion..........");
    }

    @PostMapping("/profile_changeEmotion")
    public String postChangeEmotion(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, String emotion, Model model) {
        log.info("postChangeEmotion..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            log.info("기존 회원의 감정 : " + member.getEmotion());
            log.info("변경할 감정 : " + emotion);
            member.setEmotion(emotion);
            memberRepository.save(member);
            log.info("변경 후 회원의 감정 : " + member.getEmotion());
            MemberDTO memberDTO = entityToDto(member);
            model.addAttribute("msg", "감정 변경이 완료되었습니다.");
            model.addAttribute("memberDTO", memberDTO);
            return "profile_main";
        }
        return "redirect:/logout";
    }
    /* ***** */
    private MemberDTO entityToDto(Member member) {
        MemberDTO memberDTO = MemberDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .formSocial(member.isFormSocial())
                .socialType(member.getSocialType())
                .interest(member.getInterest())
                .emotion(member.getEmotion())
                .checkLogin(true)
                .build();

        Iterator<MemberRole> it = member.getRoleSet().iterator();
        if(it.hasNext()) {
            if (it.next().toString().equals("ADMIN")) {
                memberDTO.setAdmin(true);
            }
        }
        return memberDTO;
    }
}
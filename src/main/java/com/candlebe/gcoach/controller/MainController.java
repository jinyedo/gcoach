package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.InterestDTO;
import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final ContentService contentService;
    private final MemberRepository memberRepository;

    //메인(호흡훈련)으로 이동
    @GetMapping("/")
    public String mainPage(Model model) {
        log.info("main Page..........");
        // 호흡 관련 콘텐츠 리스트 가져오기
        List<Content> contents = contentService.findContentsByCategory("호흡");
        // 가져온 콘텐츠 리스트 model 에 할당
        model.addAttribute("contents", contents);
        // 메인 페이지로 리턴
        return "main";
    }

    //메인(명상)으로 이동 - 호흡 훈련과 같은 서비스 구조로 동작
    @GetMapping("/meditation")
    public String meditationPage(Model model) {
        log.info("meditation Page..........");
        List<Content> contents = contentService.findContentsByCategory("명상");

        model.addAttribute("contents", contents);

        return "main_meditation";
    }

    //메인(추천콘텐츠)로 이동
    @GetMapping("/recommend")
    public String recommendPage(Model model, Authentication authentication, Principal principal) {
        log.info("recommend Page..........");
        // 사요아 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String findName;
        boolean social;

        // 인반 사용자
        if(userDetails == null){
            findName = principal.getName();
            social = false;

        // 소셜 사용자
        } else {
            findName = userDetails.getUsername();
            social = true;

        }

        // 위의 정보로 사용자 정보 조회
        Optional<Member> result = memberRepository.findByUsername(findName, social);
        Member member;

        // 알번 사용자
        if (result.isEmpty()) {
            Optional<Member> findMember = memberRepository.findByUsername(findName);
            member = findMember.get();

        // 소셜 사용자
        } else {
            member = result.get();
        }

        // 조회한 회원의 감정과 관심 분야를 통해 추촌 콘텐츠 리스트 가져오기
        List<Content> contents = contentService.findContentsForUser(member.getInterest(), member.getEmotion());
        model.addAttribute("contents", contents);
        return "main_recommend";
    }

    //메인(카테고리)로 이동
    @GetMapping("/category")
    public String categoryPage(Model model) {
        log.info("category Page..........");
        model.addAttribute("interestDTO", new InterestDTO());
        return "main_category";
    }

    // 메인(카테고리)에서 카테고리 클릭 시 해당 카테고리에 해당하는 콘텐츠 조회 후 model 에 할당
    @GetMapping("/category/interest")
    public String categoryInterest(Model model, InterestDTO interestDTO) {
        List<Content> contents = contentService.findContentsByCategory(interestDTO.getInterest());
        model.addAttribute("contents", contents);
        model.addAttribute("interestName", interestDTO.getInterest());
        return "category_interest";
    }

    // 플레이 화면으로 이동
    @GetMapping("/{id}/play")
    public String getPlay(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                          @PathVariable("id") Long id, Model model) throws JsonProcessingException {

        log.info("getPlay..........");
        // 사용자 정보와 콘텐츠 조회 후
        Optional<Member> result = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        Optional<Content> content = contentService.findOne(id);
        System.out.println(content.get().getCid());
        System.out.println(content.get().getOriginalName());
        if (result.isPresent()) {
            // 값이 있다면 필요 정보들을 playDTO 에 담아 play 페이지로 이동
            Member member = result.get();
            PlayDTO playDTO = PlayDTO.builder()
                    .mid(member.getMid())
                    .nickname(member.getNickname())
                    .cid(content.get().getCid())
                    .contentName(content.get().getOriginalName())
                    .build();
            log.info(playDTO);
            model.addAttribute("dto", playDTO);
        }

        return "play";
    }
}

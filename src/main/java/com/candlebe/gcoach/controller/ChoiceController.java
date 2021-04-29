package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.EmotionDTO;
import com.candlebe.gcoach.dto.InterestDTO;
import com.candlebe.gcoach.service.ChoiceEmotionService;
import com.candlebe.gcoach.service.ChoiceInterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChoiceController {

    private final ChoiceInterestService choiceInterestService;
    private final ChoiceEmotionService choiceEmotionService;

    @GetMapping("/choice/interest")
    public String createInterestForm(Model model, Authentication authentication, Principal principal) {
        log.info("choice_interest Page..........");
        // 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String findName;
        boolean social;

        // 일반 사용자
        if(userDetails == null){
            findName = principal.getName();
            social = false;

        // 소셜 사용자
        } else {
            findName = userDetails.getUsername();
            social = true;
        }

        boolean result = choiceInterestService.isInterest(findName, social);

        if(result) {
            return "redirect:/";
        } else {
            model.addAttribute("interestDTD", new InterestDTO());
            return "choice_interest";
        }
    }

    @PostMapping("/choice/interest")
    public String choiceInterest(InterestDTO interestDTO, Authentication authentication, Principal principal) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String findName;
        boolean social;

        if(userDetails == null){
            findName = principal.getName();
            social = false;
        } else {
            findName = userDetails.getUsername();
            social = true;
        }

        choiceInterestService.choiceInterest(findName, interestDTO.getInterest(), social);
        return "redirect:/choice/emotion";
    }

    @GetMapping("/choice/emotion")
    public String createEmotionForm(Model model, Authentication authentication, Principal principal) {
        log.info("choice_emotion Page..........");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String findName;
        boolean social;

        if(userDetails == null){
            findName = principal.getName();
            social = false;
        } else {
            findName = userDetails.getUsername();
            social = true;
        }
        boolean result = choiceEmotionService.isEmotion(findName, social);

        if(result) {
            return "redirect:/";
        } else {
            model.addAttribute("emotionDTD", new EmotionDTO());
            return "choice_emotion";
        }
    }

    @PostMapping("/choice/emotion")
    public String choiceEmotion(EmotionDTO emotionDTO, Authentication authentication, Principal principal) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String findName;
        boolean social;

        if (userDetails == null) {
            findName = principal.getName();
            social = false;
        } else {
            findName = userDetails.getUsername();
            social = true;
        }

        choiceEmotionService.choiceEmotion(findName, emotionDTO.getEmotion(), social);
        return "redirect:/";
    }
}

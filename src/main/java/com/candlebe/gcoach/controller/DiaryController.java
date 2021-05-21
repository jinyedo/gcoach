package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.TodayDTO;
import com.candlebe.gcoach.dto.TodayReturnEmotionTodaysreviewDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.DiaryRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class DiaryController {

    private final DiaryService diaryService;
    private final MemberRepository memberRepository;

    // 달력
    @GetMapping("/diary/calendar")
    public String calendar() {
        return "diary_calendar";
    }

    // 오늘의 후기
    @GetMapping("/diary/today/{year}/{month}/{date}")
    public String today(Model model,
                        @PathVariable("year") String year,
                        @PathVariable("month") String month,
                        @PathVariable("date") String date,
                        @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {

        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        model.addAttribute("todayDTO", new TodayDTO());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("date", date);
        try {
            // 오늘의 후기 작성한거 불러오기
            TodayReturnEmotionTodaysreviewDTO result = diaryService.getEmotionTodaysreview(year, month, date, member);
            model.addAttribute("emotion", result.getEmotion());
            model.addAttribute("todaysReview", result.getTodaysReview());
        } catch (Exception e) {
            System.out.println("오늘의 후기 작성 안되어 있으면 그냥 페이지 열어라.");
        } finally {
            return "diary_today";
        }
    }

    @PostMapping("/diary/today/{year}/{month}/{date}")
    public String saveToday(Model model, TodayDTO todayDTO,
                            @PathVariable("year") String year,
                            @PathVariable("month") String month,
                            @PathVariable("date") String date,
                            @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();

        diaryService.save(todayDTO.getEmotion(), todayDTO.getTodaysReview(), member, year, month, date);
        return "redirect:/diary/today/{year}/{month}/{date}";
    }

}

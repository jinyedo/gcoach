package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.TodayReturnEmotionTodaysreviewDTO;
import com.candlebe.gcoach.entity.Diary;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public Long save(String emotion, String todaysReview, Member member, String year, String month, String date) {
        try {
            Diary result = diaryRepository.findByDateAndMember(year, month, date, member).get(0);
            if (emotion!=result.getEmotion()) result.setEmotion(emotion); else result.setEmotion(result.getEmotion());
            if (todaysReview!=result.getTodaysReview()) result.setTodaysReview(todaysReview); else result.setTodaysReview(result.getTodaysReview());
            return result.getId();
        } catch (Exception e) {
            System.out.println("등록된 오늘의 후기가 없으면 예외처리");
        }
        Diary diary = new Diary(emotion, todaysReview, member, year, month, date);
        diaryRepository.save(diary);
        return diary.getId();
    }

    // 오늘의 감정, 오늘의 후기 가져오기
    public TodayReturnEmotionTodaysreviewDTO getEmotionTodaysreview(String year, String month, String date, Member member) {
        Diary diary = diaryRepository.findByDateAndMember(year, month, date, member).get(0);

        TodayReturnEmotionTodaysreviewDTO result = new TodayReturnEmotionTodaysreviewDTO();
        result.setEmotion(diary.getEmotion());
        result.setTodaysReview(diary.getTodaysReview());
        return result;
    }
}

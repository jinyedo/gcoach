package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChoiceEmotionService {
    private final MemberRepository memberRepository;

    // 감정 추가
    @Transactional
    public void choiceEmotion(String name, String emotion, boolean social) {
        Optional<Member> byUsername = memberRepository.findByUsername(name, true);
        Member member;
        if (byUsername.isEmpty()) {
            Optional<Member> findMember = memberRepository.findByUsername(name);
            member = findMember.get();
        } else {
            member = byUsername.get();
        }

        member.setEmotion(emotion);
    }

    // 사용자가 감정을 선택했는지 조회
    public boolean isEmotion(String name, boolean social) {
        Optional<Member> byUsername = memberRepository.findByUsername(name, social);
        Member member;
        if (byUsername.isEmpty()) {
            Optional<Member> findMember = memberRepository.findByUsername(name);
            member = findMember.get();
        } else {
            member = byUsername.get();
        }
        if (member.getEmotion() == "" || member.getEmotion() == null) {
            return false;
        } else {
            return true;
        }
    }
}

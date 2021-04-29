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
public class ChoiceInterestService {

    private final MemberRepository memberRepository;

    // 관심분야 추가
    @Transactional
    public void choiceInterest(String name, String interest, boolean social) {
        Optional<Member> byUsername = memberRepository.findByUsername(name, social);
        Member member;
        if (byUsername.isEmpty()) {
            Optional<Member> findMember = memberRepository.findByUsername(name);
            member = findMember.get();
        } else {
            member = byUsername.get();
        }

        member.setInterest(interest);
    }

    // 사용자 관심분야가 선택되었는지 확인
    public boolean isInterest(String name, boolean social) {
        // 사용자 조회
        Optional<Member> byUsername = memberRepository.findByUsername(name, social);
        Member member;

        // 일반 사용자
        if (byUsername.isEmpty()) {
            Optional<Member> findMember = memberRepository.findByUsername(name);
            member = findMember.get();

        // 소셜 로그인 사용자
        } else {
            member = byUsername.get();
        }

        // 관심 분야가 선태되었다면 true | 아니면 false
        if (member.getInterest() == "" || member.getInterest() == null) {
            return false;
        } else {
            return true;
        }
    }

}

package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService{

    private final MemberRepository memberRepository;

    // 회원가입 시, 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Override
    public String join(MemberDTO memberDTO) {
        log.info("memberDTO : " + memberDTO);

        Optional<Member> result = memberRepository.findByUsername(memberDTO.getUsername(),false);

        if (result.isPresent()) {
            return "fail";
        }

        Member member = dtoToEntity(memberDTO);
        memberRepository.save(member);
        return "success";
    }
}

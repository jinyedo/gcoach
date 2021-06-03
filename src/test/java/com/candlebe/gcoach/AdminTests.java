package com.candlebe.gcoach;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.stream.IntStream;

@SpringBootTest
public class AdminTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Commit
    public void insert() {
        Member member = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode("1111"))
                .name("관리자1")
                .nickname("관리자1")
                .phone("010-8234-0652")
                .build();
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
    }

    @Test
    public void testSearchPage() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("mid").descending()
        );
        Page<Object[]> result = memberRepository.searchPage("n", "jin", pageable);
    }

    @Test
    @Commit
    public void insertUser() {
        IntStream.rangeClosed(4, 100).forEach(i -> {
            Member member = Member.builder()
                    .username("user" + i)
                    .password("1111")
                    .name("USER" + i)
                    .nickname("USER" + i)
                    .phone("01012345678")
                    .formSocial(false)
                    .build();
            member.addMemberRole(MemberRole.USER);
            memberRepository.save(member);
        });
    }
}

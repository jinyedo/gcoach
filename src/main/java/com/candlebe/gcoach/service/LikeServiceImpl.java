package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Likes;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikeServiceImpl implements LikeService {

    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;

    public boolean addLike(Long mid, Long cid) {
        Member member = memberRepository.findById(mid).orElseThrow();
        Content content = contentRepository.findById(cid).orElseThrow();

        Likes likes = Likes.builder()
                .member(member)
                .content(content)
                .build();

        //중복 좋아요 방지
        if(isNotAlreadyLike(member, content)) {
            likeRepository.save(likes);
            return true;
        } else {
            Long lid = likeRepository.findLikes(member, content);
            likeRepository.deleteById(lid);
            return false;
        }
    }

    //사용자가 이미 좋아요 한 게시물인지 체크
    private boolean isNotAlreadyLike(Member member, Content content) {
        return likeRepository.findByMemberAndContent(member, content).isEmpty();
    }
}

package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.service.LikeService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepository;
    private final LikeService likeService;

    @PostMapping("/like")
    public String addLike(@RequestBody PlayDTO dto) {
        int likeCount = 0;
        boolean result;
        result = likeService.addLike(dto.getMid(), dto.getCid());
        likeCount = likeRepository.likeCount();
        return "{\"result\":"+ result + ",\"likeCount\":" + likeCount + "}";
    }
}

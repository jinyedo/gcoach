package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.Reply;

public interface LikeService {

    boolean addLike(Long mid, Long cid);
}

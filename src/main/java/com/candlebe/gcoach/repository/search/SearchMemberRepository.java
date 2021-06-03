package com.candlebe.gcoach.repository.search;

import com.candlebe.gcoach.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMemberRepository {

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}



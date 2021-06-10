package com.candlebe.gcoach.repository.search;

import com.candlebe.gcoach.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchContentRepository {

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}

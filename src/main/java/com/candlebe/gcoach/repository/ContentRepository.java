package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // 해당 카테고리로 콘텐츠 조회
    @Query("SELECT t FROM Content t WHERE t.category1 = :category or t.category2 = :category or t.category3 = :category")
    List<Content> findByCategory(String category);

    // 관심 분야와 감정으로 추천 콘텐츠 조회
    @Query("SELECT t FROM Content t WHERE t.category1 = :interest or t.category1 = :emotion " +
            "                          or t.category2 = :interest or t.category2 = :emotion" +
            "                          or t.category3 = :interest or t.category3 = :emotion")
    List<Content> findByUsers(String interest, String emotion);

    @Query("SELECT t FROM Content t WHERE t.cid = :id")
    Optional<Content> findById(Long id);

}

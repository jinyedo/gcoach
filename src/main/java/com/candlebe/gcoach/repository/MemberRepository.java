package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m " +
            "WHERE m.formSocial = :social AND m.username = :username")
    Optional<Member> findByUsername(String username, boolean social);

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findByUsername(String username);
}


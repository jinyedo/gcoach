package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m " +
            "WHERE m.formSocial = :social AND m.username = :username")
    Optional<Member> findByUsername(String username, boolean social);

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findByUsername(String username);

    Optional<Member> findMemberByNickname(String nickname);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.nickname = :nickname WHERE m.username = :username")
    void updateNickname(String nickname, String username);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.password = :password WHERE m.username = :username")
    void updatePassword(String password, String username);
}


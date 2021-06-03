package com.candlebe.gcoach.admin.service;

import com.candlebe.gcoach.admin.dto.AdminMemberDTO;
import com.candlebe.gcoach.admin.dto.PageRequestDTO;
import com.candlebe.gcoach.admin.dto.PageResultDTO;
import com.candlebe.gcoach.entity.Member;

public interface AdminService {

    PageResultDTO<AdminMemberDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    default AdminMemberDTO entityToDTO(Member member) {
        return AdminMemberDTO.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .formSocial(member.isFormSocial())
                .socialType(member.getSocialType())
                .interest(member.getInterest())
                .emotion(member.getEmotion())
                .build();
    }
}

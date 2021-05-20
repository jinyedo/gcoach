package com.candlebe.gcoach.dto;

import com.candlebe.gcoach.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodayDTO {

    private String emotion;
    private String todaysReview;
    private Member member;

}

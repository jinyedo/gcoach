package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    private String title;

    private String path;

    private String type;
}

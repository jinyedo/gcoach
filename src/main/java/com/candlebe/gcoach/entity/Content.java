package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    Set<Likes> likes = new HashSet<>();

    private int likeCount;

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}

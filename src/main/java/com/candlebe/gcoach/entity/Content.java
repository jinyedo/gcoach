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
    @Builder.Default
    Set<Likes> likesSet = new HashSet<>();

    public void addLikes(Likes likes) {
        likesSet.add(likes);
    }

    public void deleteLikes(Likes likes) {
        likesSet.remove(likes);
    }

    private int likeCount;

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}

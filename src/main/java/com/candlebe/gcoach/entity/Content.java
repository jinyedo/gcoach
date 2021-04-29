package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "creator")
public class Content extends BaseEntity {

    public Content(String category1, String category2, String category3, String title, String path, String originalName) {
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.title = title;
        this.path = path;
        this.originalName = originalName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    private String title;

    private String path;

    private String type;

    private String originalName;

    private String category1;
    private String category2;
    private String category3;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Reply> replies = new ArrayList<>();

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

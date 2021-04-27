package com.candlebe.gcoach.content;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContentTests {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    public void insertContent() {
        Content content = Content.builder()
                .creator(Member.builder().mid(1L).build())
                .path("C:/daelim/gcoach/content/audio/asmr/hometown.mp3")
                .title("hometown.mp3")
                .type("asmr")
                .likeCount(0)
                .build();
        contentRepository.save(content);
    }
}

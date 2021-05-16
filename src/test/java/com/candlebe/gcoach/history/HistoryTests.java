package com.candlebe.gcoach.history;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.QHistory;
import com.candlebe.gcoach.repository.HistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@Transactional
public class HistoryTests {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private EntityManager em;


    @Test
    public void testHistory() {
        List<History> histories = historyRepository.getHistoryWithAll(Member.builder().mid(2L).build());
        for (History history : histories) {
            System.out.println(history);
            System.out.println(history.getRegDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")));
        }
    }
}

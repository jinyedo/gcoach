package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.HistoryDTO;
import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;

    @Override
    public List<HistoryDTO> getHistories(Member member) {
        List<HistoryDTO> result = new ArrayList<>();
        List<History> histories = historyRepository.getHistoryWithAll(member);
        log.info(histories.size());
        for (History i : histories) {
            LocalDateTime now = i.getRegDate();
            Date d = java.sql.Timestamp.valueOf(now);
            String time = txtDate(d);

            HistoryDTO historyDTO = HistoryDTO.builder()
                    .content(i.getContent())
                    .time(time)
                    .build();
            result.add(historyDTO);
        }
        return result;
    }
}

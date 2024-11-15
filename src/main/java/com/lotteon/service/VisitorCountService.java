package com.lotteon.service;

import com.lotteon.entity.VisitorCount;
import com.lotteon.repository.VisitorCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitorCountService {

    private final VisitorCountRepository visitorCountRepository;

    public long getVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        return visitorCount.getCount();
    }
    public long getYesterdayVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        return visitorCount.getYesterdayCount() != null ? visitorCount.getYesterdayCount() : 0; // null 체크
    }

    public long getTodayVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());
        return visitorCount.getTodayCount() != null ? visitorCount.getTodayCount() : 0; // null 체크
    }

    public void incrementVisitorCount() {
        VisitorCount visitorCount = visitorCountRepository.findById(1L).orElse(new VisitorCount());

        // 오늘 방문자 수 증가
        visitorCount.setCount(visitorCount.getCount() + 1);
        visitorCount.setTodayCount(visitorCount.getTodayCount() + 1);

        // 어제 방문자 수를 오늘 카운트에 업데이트하는 로직
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() == 0 && now.getMinute() == 0) { // 매일 자정에 어제 카운트 업데이트
            visitorCount.setYesterdayCount(visitorCount.getTodayCount());
            visitorCount.setTodayCount(0L); // 오늘 카운트 초기화
        }

        visitorCountRepository.save(visitorCount);
    }
}

package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.order.Order;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {


    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public Point createPoint(Long memberId, int amount, String description) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Point point = new Point();
        point.setAmount(amount);
        point.setRemainingPoints(amount); // 초기 잔여 포인트 설정
        point.setDescription(description);
        point.setMember(member); // 포인트와 멤버 연결
        Point savedPoint = pointRepository.save(point);
        log.info("Saved point: {}", savedPoint);

        return savedPoint;
    }

    @Transactional
    public void savePoint(Member member, double earnedPoints) {
        // Point 엔티티 생성
        Point point = new Point();
        point.setMember(member);
        point.setAmount(earnedPoints);
        point.setDescription("구매 확정 포인트 적립");

        // 포인트 기록 저장
        Point savedpoint = pointRepository.save(point);

        // Member의 총 포인트 업데이트
//        member.setPoints(savedpoint);
            member.savePoint(earnedPoints);

            memberRepository.save(member);
    }
    public Page<Point> myPoints(String uid, Pageable pageable) {
        log.info("내 포인트 조회 요청");

        Optional<Member> memberOpt = memberRepository.findByUid(uid);

        Member member = memberOpt.get();

        Page<Point> points = pointRepository.findByMemberId(member.getId(), pageable);

        log.info("멤버 아이디" + member.getUid());
        // 포인트 리스트 각 항목 출력
        log.info("포인트: " + points);
        return points;

    }

    public double  getTotalPoints(String uid) {
        log.info("내 포인트 조회 요청");

        // 회원 조회
        Optional<Member> memberOpt = memberRepository.findByUid(uid);
        Member member = memberOpt.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // Member 엔티티에서 totalPoints나 remainingPoints 직접 조회
        double totalPoints = member.getPoint();  // 또는 getRemainingPoints() 사용할 수 있음

        log.info("멤버 아이디: " + member.getUid());
        log.info("총 포인트: " + totalPoints);

        return totalPoints;  // 최종 포인트 반환
    }



}

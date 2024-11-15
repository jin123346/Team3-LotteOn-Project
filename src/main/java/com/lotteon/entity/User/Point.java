package com.lotteon.entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = "member")  // member 필드를 toString에서 제외
@NoArgsConstructor
@Builder
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double usedPoint; //사용포인트
    private double amount; // 지급 or 사용 포인트
    private double remainingPoints; // 잔여 포인트
    private String description; // 지급 내용
    private long orderItemId;
    private boolean confirm; //
    private long orderId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime limitDate;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false) // member 테이블의 id와 조인
    private Member member; // Member와의 관계


    @PostPersist
    public void saveLImitDate(){

        limitDate = LocalDateTime.now().plusYears(1);
    }

}

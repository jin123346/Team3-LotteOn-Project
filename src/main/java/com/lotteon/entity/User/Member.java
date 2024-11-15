package com.lotteon.entity.User;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.dto.User.Grade;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Table(name="Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 설정
    private long id;

    private String name;

    private String gender;

    private String email;

    private String hp;

    private String postcode;

    private String addr;

    private String addr2;

    @Builder.Default
    private double point = 0.0;  // 기본값 설정

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Point> points = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.FAMILY;  // 기본값 설정

    private String userinfocol;

    @CreationTimestamp
    private LocalDateTime regDate;

    @CreationTimestamp
    private LocalDateTime lastDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberStatus status = MemberStatus.ACTIVE;  // 기본값 설정

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_uid") // 외래 키
    @JsonBackReference
    private User user; // User와의 관계

    private long totalOrder;

    public String getUid() {
        return user != null ? user.getUid() : null; // User가 null이 아닐 경우 uid 반환
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Delivery> deliveryList = new ArrayList<>();

    public void addDelivery(Delivery delivery) {
        deliveryList.add(delivery);
        delivery.setMember(this);  // 연관 관계 설정
    }

    public void savePoint(double point) {
        this.point += point;
    }
    public void usedPoint(double point){this.point-= point;}
    public void saveTotalOrder(long saveOrderPrice){this.totalOrder += saveOrderPrice;}
    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : null;
    }
}


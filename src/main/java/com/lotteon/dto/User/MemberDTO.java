package com.lotteon.dto.User;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.lotteon.entity.User.MemberStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 추가
@ToString
public class MemberDTO {

    private long id;            // Member의 id
    private String name;        // Member의 name
    private String gender;      // Member의 gender
    private String email;       // Member의 email
    private String hp;          // Member의 hp
    private String postcode;    // Member의 postcode
    private String addr;        // Member의 addr
    private String addr2;       // Member의 addr2
    private BigDecimal point;   // Member의 point
    private Grade grade;        // Member의 grade
    private MemberStatus status; // Member의 status
    private String uid;         // User의 uid
    private LocalDate regDate;  // Member의 regDate
    private LocalDate lastDate; // Member의 lastDate

    public Long getMemberId() {
        return this.id;
    }
}

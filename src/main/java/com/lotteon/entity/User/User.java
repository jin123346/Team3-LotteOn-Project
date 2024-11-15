package com.lotteon.entity.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="USER")
public class User{

    @Id
    private String uid;

    private String pass;

    @Enumerated(EnumType.STRING)
    private Role role; // enum 사용


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // Member에 의해 매핑됨
    @JsonManagedReference // 순환 참조 방지
    private Member member; // User와 Member의 관계

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // Seller와의 관계
    @JsonIgnore
    private Seller seller; // User와 Seller의 관계

    public enum Role {
        MEMBER, SELLER, ADMIN
    }

}

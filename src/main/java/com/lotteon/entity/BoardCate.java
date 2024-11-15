package com.lotteon.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotteon.entity.product.ProductCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name="boardcate")
@Entity
public class BoardCate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCateId;


    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parentId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference // 부모 참조를 직렬화하지 않도록 설정
    private BoardCate parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<BoardCate> children = new ArrayList<>();


    private String name;

 
}

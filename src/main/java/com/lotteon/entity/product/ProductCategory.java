package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "ProductCategory")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<ProductCategory> children = new ArrayList<>();

    private String name;
    private int level;
    private String subcategory;

    @Builder.Default
    private String disp_yn="y"; //디스플레이 유무
    private String note;
    // 경로를 재귀적으로 생성하는 메서드
    public String getFullPath() {
        if (parent == null) {
            return name;
        } else {
            return parent.getFullPath() + "/" + name;
        }
    }

}

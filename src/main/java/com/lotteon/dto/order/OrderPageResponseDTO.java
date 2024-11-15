package com.lotteon.dto.order;


import com.lotteon.entity.order.Order;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@ToString
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageResponseDTO {

    private Order order;

}

package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private double totalPrice;
    private ProductDto product;
//    private OrderDto order;
}

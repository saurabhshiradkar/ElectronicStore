package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId;

    //PENDING DISPATCHED DELIVERED
    private String orderStatus="PENDING";

    //NOT-PAID PAID
    private  String paymentStatus="NOTPAID";

    private double orderAmount;

    private String billingAddress;

    private  String billingPhone;

    private String billingName;

    private Date orderedDate = new Date();

    private Date deliveredDate;

    private UserDto user;

    private String razorPayOrderId;

    private String paymentId;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}

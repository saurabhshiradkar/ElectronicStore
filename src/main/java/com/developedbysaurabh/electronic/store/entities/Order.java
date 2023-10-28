package com.developedbysaurabh.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;

    //PENDING DISPATCHED DELIVERED
    private String orderStatus;

    //NOT-PAID PAID
    private  String paymentStatus;

    private double orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private  String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}

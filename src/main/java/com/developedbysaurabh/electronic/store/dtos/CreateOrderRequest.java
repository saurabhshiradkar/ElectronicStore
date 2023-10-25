package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {


    @NotBlank(message = "User Id Is Required !")
    private String userId;
    @NotBlank(message = "Cart Id Is Required !")
    private String cartId;
    private String orderStatus="PENDING";
    private  String paymentStatus="NOTPAID";
    @NotBlank(message = "Address Is Required !")
    private String billingAddress;
    @NotBlank(message = "Contact Is Required !")
    private  String billingPhone;
    @NotBlank(message = "Billing Name Is Required !")
    private String billingName;
}

package com.developedbysaurabh.electronic.store.services;

import com.developedbysaurabh.electronic.store.dtos.CreateOrderRequest;
import com.developedbysaurabh.electronic.store.dtos.OrderDto;
import com.developedbysaurabh.electronic.store.dtos.OrderUpdateRequest;
import com.developedbysaurabh.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createdOrder(CreateOrderRequest createOrderRequest);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);

    //other methods

    //Update Order
    OrderDto updateOrder(String orderId, OrderUpdateRequest request);
    OrderDto updateOrder(String orderId, OrderDto request);

    OrderDto getOrder(String orderId);
}

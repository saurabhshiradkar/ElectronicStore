package com.developedbysaurabh.electronic.store.services.impl;

import com.developedbysaurabh.electronic.store.dtos.CreateOrderRequest;
import com.developedbysaurabh.electronic.store.dtos.OrderDto;
import com.developedbysaurabh.electronic.store.dtos.OrderUpdateRequest;
import com.developedbysaurabh.electronic.store.dtos.PageableResponse;
import com.developedbysaurabh.electronic.store.entities.*;
import com.developedbysaurabh.electronic.store.exceptions.BadApiRequestException;
import com.developedbysaurabh.electronic.store.exceptions.ResourceNotFoundException;
import com.developedbysaurabh.electronic.store.helper.Helper;
import com.developedbysaurabh.electronic.store.repositories.CartRepository;
import com.developedbysaurabh.electronic.store.repositories.OrderRepository;
import com.developedbysaurabh.electronic.store.repositories.ProductRepository;
import com.developedbysaurabh.electronic.store.repositories.UserRepository;
import com.developedbysaurabh.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderDto createdOrder(CreateOrderRequest orderDto) {

        //fetch user
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given ID !"));

        //fetch cart
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found With Given ID !"));

        List<CartItem> cartItems = cart.getItems();


        if(cartItems.size() == 0){
            throw new BadApiRequestException("Invalid Number of items in Cart !");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        //orderItems amount are not set

        AtomicReference<Double> orderAmount = new AtomicReference<>(0.00);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {


            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //cart clear
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder,OrderDto.class);
    }


    @Override
    public OrderDto updateOrder(String orderId, OrderUpdateRequest request) {

        //get the order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadApiRequestException("Invalid update data"));
        order.setBillingName(request.getBillingName().isBlank()?order.getBillingName():request.getBillingName());
        order.setBillingPhone(request.getBillingPhone().isBlank()?order.getBillingPhone():request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress().isBlank()?order.getBillingAddress():request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus().isBlank()?order.getPaymentStatus():request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus().isBlank()?order.getOrderStatus():request.getOrderStatus());
        request.getDeliveredDate();
        order.setDeliveredDate(request.getDeliveredDate());
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderDto request) {

        //get the order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadApiRequestException("Invalid update data"));

        order.setBillingName(request.getBillingName().isBlank()?order.getBillingName():request.getBillingName());
        order.setBillingPhone(request.getBillingPhone().isBlank()?order.getBillingPhone():request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress().isBlank()?order.getBillingAddress():request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus().isBlank()?order.getPaymentStatus():request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus().isBlank()?order.getOrderStatus():request.getOrderStatus());
        request.getDeliveredDate();
        order.setDeliveredDate(request.getDeliveredDate());

        order.setRazorPayOrderId(request.getRazorPayOrderId());
        order.setPaymentId(request.getPaymentId());

        Order updatedOrder = orderRepository.save(order);

        return mapper.map(updatedOrder, OrderDto.class);

    }

    @Override
    public OrderDto getOrder(String orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found With Given Id"));

        OrderDto orderDto = mapper.map(order, OrderDto.class);

        return orderDto;
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found !"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found !"));

        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtoList = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());

        return orderDtoList;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page,OrderDto.class);
    }


}

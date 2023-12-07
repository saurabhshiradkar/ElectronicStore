package com.developedbysaurabh.electronic.store.controllers;

import com.developedbysaurabh.electronic.store.dtos.OrderDto;
import com.developedbysaurabh.electronic.store.dtos.UserDto;
import com.developedbysaurabh.electronic.store.services.OrderService;
import com.developedbysaurabh.electronic.store.services.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Value("${razorPayKey}")
    private String razorPayKey;

    @Value("${razorPaySecret}")
    private String razorPaySecret;
    private UserService userService;
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    public PaymentController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping("/initiate-payment/{orderId}")
    public ResponseEntity<?> initiatePayment(
            @PathVariable("orderId") String orderId, Principal principal
    ){
        UserDto userDto = this.userService.getUserByEmail(principal.getName());

        OrderDto orderDto = this.orderService.getOrder(orderId);

        //Razor Pay API to create order

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorPayKey,razorPaySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",orderDto.getOrderAmount()*100);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", "receipt_orderId");

            Order order = razorpayClient.orders.create(orderRequest);

//            save the order id to backend
            orderDto.setRazorPayOrderId(order.get("id"));
//            orderDto.setPaymentStatus(order.get("status").toString().toUpperCase());
            this.orderService.updateOrder(orderDto.getOrderId(), orderDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "orderId", orderDto.getOrderId(),
                    "razorpayOrderId" ,orderDto.getRazorPayOrderId(),
                    "amount" , orderDto.getOrderAmount(),
                    "paymentStatus", orderDto.getPaymentStatus()

            ));
        }catch (RazorpayException ex){
            ex.printStackTrace();

            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message" , "error in creating order!"));
        }
    }


    @PostMapping("/capture/{orderId}")
    public ResponseEntity<?> verifyAndSavePayment(
            @RequestBody Map<String,Object> data,
            @PathVariable("orderId") String orderId
    ){
        String razorpayOrderId = data.get("razorpayOrderId").toString();
        String razorpayPaymentId = data.get("razorpayPaymentId").toString();
        String razorpayPaymentSignature = data.get("razorpayPaymentSignature").toString();

        OrderDto orderDto = this.orderService.getOrder(orderId);

        orderDto.setPaymentStatus("SUCCESS");
//        orderDto.setRazorPayOrderId(razorpayOrderId);
        orderDto.setPaymentId(razorpayPaymentId);

        //
        this.orderService.updateOrder(orderId,orderDto);

        try{

            RazorpayClient razorpayClient = new RazorpayClient(razorPayKey,razorPaySecret);


            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpayPaymentSignature);

            boolean status =  Utils.verifyPaymentSignature(options, razorPaySecret);

            if(status){
                logger.info("PAYMENT SIGNATURE VERIFIED !");

                return  new ResponseEntity<>(
                        Map.of(
                                "message" , "Payment Done!",
                                "success", true,
                                "signatureVerified", true
                        ),HttpStatus.OK);
            }else{
                logger.info("PAYMENT SIGNATURE VERIFICATION FAILED!");
                return  new ResponseEntity<>(
                        Map.of(
                                "message" , "Payment Failed!",
                                "success", true,
                                "signatureVerified", false
                        ),HttpStatus.OK);
            }

        }catch(RazorpayException ex){
            throw  new RuntimeException(ex);
        }
    }

}

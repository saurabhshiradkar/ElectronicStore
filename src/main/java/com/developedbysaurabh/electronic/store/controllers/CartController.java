package com.developedbysaurabh.electronic.store.controllers;

import com.developedbysaurabh.electronic.store.dtos.AddItemToCartRequest;
import com.developedbysaurabh.electronic.store.dtos.ApiResponseMessage;
import com.developedbysaurabh.electronic.store.dtos.CartDto;
import com.developedbysaurabh.electronic.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "CartController")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //add items to cart
    @PostMapping("/{userId}")
    @Operation(summary = "Add Item To Cart!")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable("userId") String userId,
            @RequestBody AddItemToCartRequest request
    ){
        CartDto cartDto = cartService.addItemToCart(userId, request);

        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    @Operation(summary = "Remove Item From Cart")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable("itemId") int itemId,
            @PathVariable("userId") String userId
    ){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Item is Removed !")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear Cart")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable("userId") String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Cart Cleard !")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "get user cart !")
    public ResponseEntity<CartDto> getUserCart(
            @PathVariable("userId") String userId
    ){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}

package com.developedbysaurabh.electronic.store.services;

import com.developedbysaurabh.electronic.store.dtos.AddItemToCartRequest;
import com.developedbysaurabh.electronic.store.dtos.CartDto;

public interface CartService {

    //add items to cart

    //case 1 : cart for user is not available : we will create the and then add the items

    //case 2 : cart available : add the items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart :
    void removeItemFromCart(String userId, int cartItemId);

    //Clear Cart : remove all items from the cart
    void  clearCart(String userId);

    CartDto getCartByUser(String userId);

}

package com.developedbysaurabh.electronic.store.services.impl;

import com.developedbysaurabh.electronic.store.dtos.AddItemToCartRequest;
import com.developedbysaurabh.electronic.store.dtos.CartDto;
import com.developedbysaurabh.electronic.store.entities.Cart;
import com.developedbysaurabh.electronic.store.entities.CartItem;
import com.developedbysaurabh.electronic.store.entities.Product;
import com.developedbysaurabh.electronic.store.entities.User;
import com.developedbysaurabh.electronic.store.exceptions.BadApiRequestException;
import com.developedbysaurabh.electronic.store.exceptions.ResourceNotFoundException;
import com.developedbysaurabh.electronic.store.repositories.CartItemRepository;
import com.developedbysaurabh.electronic.store.repositories.CartRepository;
import com.developedbysaurabh.electronic.store.repositories.ProductRepository;
import com.developedbysaurabh.electronic.store.repositories.UserRepository;
import com.developedbysaurabh.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.mapper = mapper;
    }

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <=0 ){
            throw new BadApiRequestException("Quantity Can not be Negative or Zero !");
        }



        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found With Given ID !"));


        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found With Given ID !"));

        Cart cart = null;

        try
        {
            cart = cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException e)
        {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedOn(new Date());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //perform cart operations


        // if cart item already present : then update quantity

        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        List<CartItem> items = cart.getItems();

        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already exists update quantity

//                logger.info("Products In Stock : {}",item.getProduct().getQuantity());
//                logger.info("Requested Quantity : {}",(item.getQuantity()+quantity));
//
//                if (item.getProduct().getQuantity() < (item.getQuantity()+quantity)){
//                    throw new BadApiRequestException("Not Enough Stock Exists For Product !");
//                }

                item.setQuantity(item.getQuantity()+ quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);


        //create items
        if (!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }


        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);

        return mapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found With Given ID !"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found With Given ID !"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Of Given User Not Found !"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found With Given ID !"));

        //fetch cart by user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Of Given User Not Found !"));
        return mapper.map(cart, CartDto.class);
    }
}

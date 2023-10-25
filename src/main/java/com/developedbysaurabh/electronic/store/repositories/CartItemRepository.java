package com.developedbysaurabh.electronic.store.repositories;

import com.developedbysaurabh.electronic.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}

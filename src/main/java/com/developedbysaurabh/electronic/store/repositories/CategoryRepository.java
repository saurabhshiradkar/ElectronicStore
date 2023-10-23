package com.developedbysaurabh.electronic.store.repositories;

import com.developedbysaurabh.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
}

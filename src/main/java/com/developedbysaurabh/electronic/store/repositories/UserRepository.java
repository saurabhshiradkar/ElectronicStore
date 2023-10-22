package com.developedbysaurabh.electronic.store.repositories;

import com.developedbysaurabh.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAAndPassword(String email,String password);

    List<User> findByNameContaining(String keywords);

}

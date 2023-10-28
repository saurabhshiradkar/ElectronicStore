package com.developedbysaurabh.electronic.store.repositories;

import com.developedbysaurabh.electronic.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByRoleName(String roleName);
}

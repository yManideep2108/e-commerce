package com.ecommerce.manideep.sb.ecom.repositories;

import com.ecommerce.manideep.sb.ecom.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser , Long> {

    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

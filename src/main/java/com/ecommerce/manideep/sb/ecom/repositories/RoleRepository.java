package com.ecommerce.manideep.sb.ecom.repositories;

import com.ecommerce.manideep.sb.ecom.model.AppRole;
import com.ecommerce.manideep.sb.ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole roleUser);
}

package com.ecommerce.manideep.sb.ecom.repositories;

import com.ecommerce.manideep.sb.ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);
}

package com.ecommerce.manideep.sb.ecom.repositories;

import com.ecommerce.manideep.sb.ecom.model.Category;
import com.ecommerce.manideep.sb.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}

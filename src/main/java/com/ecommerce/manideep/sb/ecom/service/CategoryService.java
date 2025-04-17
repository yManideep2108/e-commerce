package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.payload.CategoryDTO;
import com.ecommerce.manideep.sb.ecom.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber ,Integer pageSize);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}

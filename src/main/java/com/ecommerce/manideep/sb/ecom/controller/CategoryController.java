package com.ecommerce.manideep.sb.ecom.controller;

import com.ecommerce.manideep.sb.ecom.config.AppConstants;
import com.ecommerce.manideep.sb.ecom.payload.CategoryDTO;
import com.ecommerce.manideep.sb.ecom.payload.CategoryResponse;
import com.ecommerce.manideep.sb.ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//https://github.com/yManideep2108/e-commerce.git
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService ;

    @GetMapping("/api/public/categories")
    public ResponseEntity <CategoryResponse> getCategories (
            @RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER ,required = false) Integer pageNumber ,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY , required = false ) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIRECTION , required = false ) String sortOrder
    ){
        CategoryResponse categoryResponse = categoryService.getAllCategories( pageNumber , pageSize ,sortBy , sortOrder );
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }
    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> createCategories (@Valid  @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public  ResponseEntity<CategoryDTO> deleteCategories (@PathVariable Long categoryId) {
        CategoryDTO deleteCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<CategoryDTO>(deleteCategory, HttpStatus.OK);

    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategories (@RequestBody CategoryDTO categoryDTO ,@PathVariable Long categoryId){
        CategoryDTO savedCategoryDTO=  categoryService.updateCategory(categoryDTO,categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}

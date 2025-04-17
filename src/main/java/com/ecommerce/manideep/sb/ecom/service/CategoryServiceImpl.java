package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.exeptions.APIExeption;
import com.ecommerce.manideep.sb.ecom.exeptions.ResourseNotFoundExeption;
import com.ecommerce.manideep.sb.ecom.model.Category;
import com.ecommerce.manideep.sb.ecom.payload.CategoryDTO;
import com.ecommerce.manideep.sb.ecom.payload.CategoryResponse;
import com.ecommerce.manideep.sb.ecom.repositories.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper ;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber , Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> allCatergories = categoryPage.getContent();
        if(allCatergories.isEmpty()){
            throw new APIExeption("No Categories exists !! ");
        }
        List<CategoryDTO> categoryDTOS = allCatergories.stream()
                .map(category -> modelMapper.map(category ,CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category CategoryFromDB = categoryRepo.findByCategoryName(category.getCategoryName());
        if (CategoryFromDB != null){
            throw new APIExeption("Category " + categoryDTO.getCategoryName() + " already exists !!!");
        }
        Category savedCategory = categoryRepo.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory,CategoryDTO.class);
        return savedCategoryDTO ;

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourseNotFoundExeption("Category" ,"CategoryId",categoryId));

        categoryRepo.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourseNotFoundExeption("Category" ,"CategoryId",categoryId));
        Category category = modelMapper.map(categoryDTO , Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(category);
        return  modelMapper.map(savedCategory,CategoryDTO.class);
    }
}
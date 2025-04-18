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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper ;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber , Integer pageSize ,String sortBy ,String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending() ;
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
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
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryResponse.getTotalElements());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
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
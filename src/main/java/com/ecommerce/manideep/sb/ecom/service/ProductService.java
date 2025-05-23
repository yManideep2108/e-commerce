package com.ecommerce.manideep.sb.ecom.service;


import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductRepsonse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);


    ProductRepsonse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductRepsonse searchProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductRepsonse getAllProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

   ProductDTO updateProductById(Long productId, ProductDTO product);

    ProductDTO deleteProductById(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

}

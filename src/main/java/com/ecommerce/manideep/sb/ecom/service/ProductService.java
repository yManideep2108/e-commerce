package com.ecommerce.manideep.sb.ecom.service;


import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductRepsonse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductRepsonse getProducts();

    ProductRepsonse searchProductsByCategory(Long categoryId);

    ProductRepsonse getAllProductsByKeyword(String keyword);

   ProductDTO updateProductById(Long productId, ProductDTO product);

    ProductDTO deleteProductById(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}

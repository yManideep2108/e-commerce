package com.ecommerce.manideep.sb.ecom.controller;

import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductRepsonse;
import com.ecommerce.manideep.sb.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProducts(@RequestBody  ProductDTO productDTO,@PathVariable Long categoryId){
        ProductDTO savedproductDTO =   productService.addProduct(categoryId,productDTO);
        return new ResponseEntity<>(savedproductDTO, HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductRepsonse> getAllProducts(){
      ProductRepsonse productRepsonse=  productService.getProducts();
      return new ResponseEntity<>(productRepsonse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductRepsonse> getAllProductsByCategory(@PathVariable Long categoryId){
        ProductRepsonse productRepsonse = productService.searchProductsByCategory(categoryId);
        return new ResponseEntity<>(productRepsonse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductRepsonse> getAllProductsByKeyword(@PathVariable String keyword){
        ProductRepsonse productRepsonse = productService.getAllProductsByKeyword(keyword );
        return new ResponseEntity<>(productRepsonse,HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProducts( @RequestBody ProductDTO productDTO ,
                                                      @PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProductById(productId ,productDTO);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProducts( @PathVariable Long productId){
        ProductDTO deleteProductDTO = productService.deleteProductById(productId);
        return new ResponseEntity<>(deleteProductDTO,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public  ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId ,
                                                   @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }
}

package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.exeptions.ResourseNotFoundExeption;
import com.ecommerce.manideep.sb.ecom.model.Category;
import com.ecommerce.manideep.sb.ecom.model.Product;
import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductRepsonse;
import com.ecommerce.manideep.sb.ecom.repositories.CategoryRepo;
import com.ecommerce.manideep.sb.ecom.repositories.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper ;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category= categoryRepo.findById(categoryId)
                .orElseThrow(()->  new ResourseNotFoundExeption("Category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice =( product.getPrice()) - (product.getPrice())  * (product.getDiscount()*0.01);
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);

    }

    @Override
    public ProductRepsonse getProducts() {
     List<Product> products = productRepo.findAll();
     List<ProductDTO> productDTOList = products.stream()
             .map(product -> modelMapper.map(product, ProductDTO.class))
             .collect(Collectors.toList());

     ProductRepsonse productRepsonse = new ProductRepsonse();
     productRepsonse.setContent(productDTOList);
     return  productRepsonse;
    }

    @Override
    public ProductRepsonse searchProductsByCategory(Long categoryId) {
        Category category= categoryRepo.findById(categoryId)
                .orElseThrow(()
                        -> new ResourseNotFoundExeption("Category","categoryId",categoryId));
        List<Product> products = productRepo.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductRepsonse productRepsonse = new ProductRepsonse();
        productRepsonse.setContent(productDTOList);
        return  productRepsonse;
    }

    @Override
    public ProductRepsonse getAllProductsByKeyword(String keyword) {

        List<Product> products = productRepo.findByProductNameLikeIgnoreCase('%' + keyword +'%');
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductRepsonse productRepsonse = new ProductRepsonse();
        productRepsonse.setContent(productDTOList);
        return  productRepsonse;
    }


    @Override
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {

       Product productFromDb = productRepo.findById(productId).orElseThrow(
                ()->   new ResourseNotFoundExeption("product" ,"productId" ,productId)
        );
        Product product = modelMapper.map(productDTO, Product.class);
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(productFromDb.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct = productRepo.save(productFromDb);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProductById(Long productId) {
        Product productFromDb = productRepo.findById(productId).orElseThrow(
                ()->   new ResourseNotFoundExeption("product" ,"productId" ,productId)
        );
       productRepo.delete(productFromDb);
       return modelMapper.map(productFromDb, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //get product from DB
        Product productFromDb = productRepo.findById(productId).orElseThrow(
                ()->   new ResourseNotFoundExeption("product" ,"productId" ,productId)
        );
        //upload image to server
        //get file name of the uploaded  image
         String path ="images/";
         String fileName = uploadImage(path,image);
         productFromDb.setImage(fileName);
         Product updatedproduct = productRepo.save(productFromDb);
         return modelMapper.map(updatedproduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {

        //File name of current or original file
         String originalFileName = file.getOriginalFilename();
        //generate a unique file name
        //If file name is ball.jpg and randomuuid is 1234
        //Then file name will be 1234.jpg
         String uniqueId =  UUID.randomUUID().toString();
         String fileName =  uniqueId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        //check if path exists and create
         String filePath = path + File.separator + fileName ;
         File folder = new File(path);
         if(!folder.exists()){
             folder.mkdir();
         }
        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        //returning the file name
        return fileName ;
    }
}

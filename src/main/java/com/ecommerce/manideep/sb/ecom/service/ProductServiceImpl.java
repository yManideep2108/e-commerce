package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.exeptions.APIExeption;
import com.ecommerce.manideep.sb.ecom.exeptions.ResourseNotFoundExeption;
import com.ecommerce.manideep.sb.ecom.model.Category;
import com.ecommerce.manideep.sb.ecom.model.Product;
import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductRepsonse;
import com.ecommerce.manideep.sb.ecom.repositories.CategoryRepo;
import com.ecommerce.manideep.sb.ecom.repositories.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    private FileService fileService;
    @Value("${project.image")
    private String path ;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        //check if product is exists or not

        Category category= categoryRepo.findById(categoryId)
                .orElseThrow(()->  new ResourseNotFoundExeption("Category","categoryId",categoryId));
        boolean isProductIsPresent = true ;
        List<Product> products = category.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductName().equals(productDTO.getProductName())){
                isProductIsPresent = false ;
                break;
            }
        }
        if(isProductIsPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = (product.getPrice()) - (product.getPrice()) * (product.getDiscount() * 0.01);
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepo.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new APIExeption("Product already exists !! ") ;
        }
    }


    @Override
    public ProductRepsonse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //check if product size is 0
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageable);
        List<Product> products = pageProducts.getContent();
        if (products.isEmpty()){
            throw new APIExeption("Products does not exists !!! ");
        }
     List<ProductDTO> productDTOList = products.stream()
             .map(product -> modelMapper.map(product, ProductDTO.class))
             .collect(Collectors.toList());
     ProductRepsonse productRepsonse = new ProductRepsonse();
     productRepsonse.setContent(productDTOList);git 
     productRepsonse.setPageNumber(pageNumber);
     productRepsonse.setPageSize(pageSize);
     productRepsonse.setTotalElements(pageProducts.getTotalElements());
     productRepsonse.setTotalPage(pageProducts.getTotalPages());
     productRepsonse.setLastPage(pageProducts.isLast());
     return  productRepsonse;
    }

    @Override
    public ProductRepsonse searchProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //check if product size is 0
        Category category= categoryRepo.findById(categoryId)
                .orElseThrow(()
                        -> new ResourseNotFoundExeption("Category","categoryId",categoryId));
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepo.findByCategoryOrderByPriceAsc(category,pageable);
        List<Product> products = pageProducts.getContent();
        if (products.isEmpty()){
            throw new APIExeption(category.getCategoryName() + " does not have any products !!! ");
        }
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductRepsonse productRepsonse = new ProductRepsonse();
        productRepsonse.setContent(productDTOList);
        productRepsonse.setContent(productDTOList);
        productRepsonse.setPageNumber(pageNumber);
        productRepsonse.setPageSize(pageSize);
        productRepsonse.setTotalElements(pageProducts.getTotalElements());
        productRepsonse.setTotalPage(pageProducts.getTotalPages());
        productRepsonse.setLastPage(pageProducts.isLast());
        return  productRepsonse;
    }

    @Override
    public ProductRepsonse getAllProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //check if product size is 0
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepo.findByProductNameLikeIgnoreCase('%' + keyword +'%',pageable);
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        if (products.size() == 0 ){
            throw new APIExeption("No products exists with " +keyword);
        }
        ProductRepsonse productRepsonse = new ProductRepsonse();
        productRepsonse.setContent(productDTOList);
        productRepsonse.setContent(productDTOList);
        productRepsonse.setPageNumber(pageNumber);
        productRepsonse.setPageSize(pageSize);
        productRepsonse.setTotalElements(pageProducts.getTotalElements());
        productRepsonse.setTotalPage(pageProducts.getTotalPages());
        productRepsonse.setLastPage(pageProducts.isLast());
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
         String fileName = fileService.uploadImage(path,image);
         productFromDb.setImage(fileName);
         Product updatedproduct = productRepo.save(productFromDb);
         return modelMapper.map(updatedproduct,ProductDTO.class);
    }
}

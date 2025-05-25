package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.exeptions.APIExeption;
import com.ecommerce.manideep.sb.ecom.exeptions.ResourseNotFoundExeption;
import com.ecommerce.manideep.sb.ecom.model.Cart;
import com.ecommerce.manideep.sb.ecom.model.CartItem;
import com.ecommerce.manideep.sb.ecom.model.Product;
import com.ecommerce.manideep.sb.ecom.payload.CartDTO;
import com.ecommerce.manideep.sb.ecom.payload.ProductDTO;
import com.ecommerce.manideep.sb.ecom.repositories.CartItemRepo;
import com.ecommerce.manideep.sb.ecom.repositories.CartRepo;
import com.ecommerce.manideep.sb.ecom.repositories.ProductRepo;
import com.ecommerce.manideep.sb.ecom.utilities.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepo cartRepo ;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart = createCart();
        //Retrieve product details
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourseNotFoundExeption("Product","productId",productId));
        //Perform validations
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if (cartItem != null){
            throw new APIExeption("Product " + product.getProductName() + " already exists in cart");
        }

        if(product.getQuantity() == 0){
            throw new APIExeption(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIExeption("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }
        //create cart item
        CartItem newcartItem = new CartItem();
        newcartItem.setProduct(product);
        newcartItem.setCart(cart);
        newcartItem.setQuantity(quantity);
        newcartItem.setDiscount(product.getDiscount());
        newcartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepo.save(newcartItem);

        cart.setTotalPrice(cart.getTotalPrice() +( product.getSpecialPrice() * quantity ));
        //update cart item
        cartRepo.save(cart);
        //return updated cart item
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item ->{
            ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if (carts.isEmpty()){
            throw new APIExeption("No cart items were added !!!");
        }
        List<CartDTO> cartDTOS= carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream()
                    .map(cartItem -> {
                        ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                        productDTO.setQuantity(cartItem.getQuantity());
                        return productDTO ;
                    }).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndcartId(emailId,cartId);
        if (cart == null){
            throw new ResourseNotFoundExeption("Cart ","cart id ",cartId);
        }
        cart.getCartItems().forEach(c -> {
            c.getProduct().setQuantity(c.getQuantity());
        });
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List <ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateCartProductQuantityInCart(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepo.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(()->new ResourseNotFoundExeption("Cart","cart id ",cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(()->new ResourseNotFoundExeption("Cart","cart id ",productId));

        if(product.getQuantity() == null){
            throw new APIExeption(product.getProductName() + " is not available");
        }

        if(product.getQuantity() < quantity){
            throw new APIExeption("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cartId);
        if (cartItem == null){
            throw new APIExeption("Product " + product.getProductName() + " not available in the cart!!!");
        }

        // Updating quantities
        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0){
            throw new APIExeption("The resulting quantity cannot be negative.");
        }
        if(newQuantity == 0){
            deleteProductFromCart(cartId,productId);
        }
        else{
                cartItem.setProductPrice(product.getSpecialPrice());
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setDiscount(product.getDiscount());
                cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice()*quantity));
                cartRepo.save(cart);
        }

        CartItem updatedCartItem = cartItemRepo.save(cartItem);
        if(updatedCartItem.getQuantity() == 0 ){
            cartItemRepo.deleteById(updatedCartItem.getCartItemId());
            log.debug("Quantity is 0 for updated cart");
        }
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> products = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct() , ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        cartDTO.setProducts(products.toList());
        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> {
            throw new ResourseNotFoundExeption("Cart "," cart id ",cartId);
        });
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cartId);
        if (cartItem == null){
            throw new  ResourseNotFoundExeption("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getQuantity() * cartItem.getProductPrice()));
        cartItemRepo.deleteCartItemByProductIdAndCartId(productId,cartId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourseNotFoundExeption("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourseNotFoundExeption("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cartId);
        if (cartItem == null ){
            throw new APIExeption(product.getProductName() + " is not available");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItem = cartItemRepo.save(cartItem);
    }


    private Cart createCart(){
        Cart userCart = cartRepo.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null){
            return userCart ;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepo.save(cart);
    }


}

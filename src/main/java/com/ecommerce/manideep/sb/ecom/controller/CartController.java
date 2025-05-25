package com.ecommerce.manideep.sb.ecom.controller;

import com.ecommerce.manideep.sb.ecom.model.Cart;
import com.ecommerce.manideep.sb.ecom.payload.CartDTO;
import com.ecommerce.manideep.sb.ecom.repositories.CartRepo;
import com.ecommerce.manideep.sb.ecom.service.CartService;
import com.ecommerce.manideep.sb.ecom.utilities.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId ,@PathVariable Integer quantity ){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(carts, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public  ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepo.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO,HttpStatus.FOUND);
    }

    @PutMapping("/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO = cartService.updateCartProductQuantityInCart(productId ,
                operation.equalsIgnoreCase("delete") ? -1 : 1 );
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId ,
                                                        @PathVariable Long productId){

        String status = cartService.deleteProductFromCart(cartId,productId);

        return new ResponseEntity<String>(status,HttpStatus.OK);

    }
}

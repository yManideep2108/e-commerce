package com.ecommerce.manideep.sb.ecom.utilities;

import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    AppUserRepo appUserRepo ;


    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName()).orElseThrow(()-> {
                    throw new UsernameNotFoundException("User name " + authentication.getName()+" is not found !!" );
                });
        return  user.getEmail();
    }

    public AppUser loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName()).orElseThrow(()-> {
            throw new UsernameNotFoundException("User name " + authentication.getName()+" is not found !!" );
        });
        return user;
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName()).orElseThrow(()-> {
            throw new UsernameNotFoundException("User name " + authentication.getName()+" is not found !!" );
        });
        return user.getUserId();
    }

}

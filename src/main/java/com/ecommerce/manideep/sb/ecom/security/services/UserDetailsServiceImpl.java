package com.ecommerce.manideep.sb.ecom.security.services;

import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.repositories.AppUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AppUserRepo appUserRepo ;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found :"+username));
        return UserDetailsImpl.build(user);
    }
}

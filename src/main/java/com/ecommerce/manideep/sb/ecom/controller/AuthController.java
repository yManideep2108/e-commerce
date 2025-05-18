package com.ecommerce.manideep.sb.ecom.controller;

import com.ecommerce.manideep.sb.ecom.model.AppRole;
import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.model.Role;
import com.ecommerce.manideep.sb.ecom.repositories.AppUserRepo;
import com.ecommerce.manideep.sb.ecom.repositories.RoleRepository;
import com.ecommerce.manideep.sb.ecom.security.jwt.JwtUtils;
import com.ecommerce.manideep.sb.ecom.security.request.LoginRequest;
import com.ecommerce.manideep.sb.ecom.security.request.SignupRequest;
import com.ecommerce.manideep.sb.ecom.security.response.MessageResponse;
import com.ecommerce.manideep.sb.ecom.security.response.UserInfoResponse;
import com.ecommerce.manideep.sb.ecom.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils ;

    @Autowired
    private AppUserRepo userRepo ;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            UserInfoResponse response = new UserInfoResponse(userDetails.getId()
                    ,jwtCookie.toString()
                    , userDetails.getUsername(), roles);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(response);
        }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser( @Valid @RequestBody SignupRequest signupRequest )  {
        if(userRepo.existsByUsername(signupRequest.getUsername())){
            return new ResponseEntity<>(new MessageResponse("ERROR : User name is already taken !"),HttpStatus.BAD_REQUEST);
        }
        if(userRepo.existsByEmail(signupRequest.getEmail())){
            return new ResponseEntity<>(new MessageResponse("ERROR : Email is already taken !"),HttpStatus.BAD_REQUEST);
        }

        AppUser user = new AppUser(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRole = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRole == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);

                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        }else{
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.getId(),userDetails.getUsername(),roles
        );
        return new ResponseEntity<>(userInfoResponse,HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutuser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("You've been signed out!"));

    }

    }


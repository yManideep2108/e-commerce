package com.ecommerce.manideep.sb.ecom.security.webconfigsecurity;

import com.ecommerce.manideep.sb.ecom.model.AppRole;
import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.model.Role;
import com.ecommerce.manideep.sb.ecom.repositories.AppUserRepo;
import com.ecommerce.manideep.sb.ecom.repositories.RoleRepository;
import com.ecommerce.manideep.sb.ecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.manideep.sb.ecom.security.jwt.AuthTokenFilter;
import com.ecommerce.manideep.sb.ecom.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService ;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler ;

    @Bean
    public AuthTokenFilter buildAuthenticationTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                //.requestMatchers("/api/admin/**").permitAll()
                                //.requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(buildAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository , AppUserRepo userRepo ,
                                      PasswordEncoder passwordEncoder){
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).
                    orElseGet(()->{
                        Role newUser = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUser);
                    });
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(()->{
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole,sellerRole,adminRole);

            if(!userRepo.existsByUsername("user1")){
                AppUser user1 = new AppUser("user1",
                        "user@ecom.com", passwordEncoder.encode("password1"));
                userRepo.save(user1);
            }

            if(!userRepo.existsByUsername("seller1")){
                AppUser seller1 = new AppUser("seller1",
                        "seller@ecom.com", passwordEncoder.encode("password2"));
                userRepo.save(seller1);
            }

            if(!userRepo.existsByUsername("admin1")){
                AppUser admin1 = new AppUser("admin1",
                        "admin@ecom.com", passwordEncoder.encode("password3"));
                userRepo.save(admin1);
            }
            //update user for roles
            userRepo.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepo.save(user);
            });

            userRepo.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepo.save(seller);
            });

            userRepo.findByUsername("admin1").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepo.save(admin);
            });
        };

    }
}

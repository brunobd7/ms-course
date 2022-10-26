package com.dantas.hroauth.services;

import com.dantas.hroauth.entities.User;
import com.dantas.hroauth.feignclients.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    public User findByEmail(String email){
        logger.info("Call feign user -> search email");
        return Optional.ofNullable(userFeignClient.findByEmail(email).getBody())
                .orElseThrow( () -> new IllegalArgumentException("Email not found"));
    }

    /**
     * OVERRIDING USER DETAILS SERVICE INTERFACE OF SPRING SECURITY, TO SEARCH USER BY "userName" ON DATABASE
     * */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        logger.info("Call loadByUserName -> search by username for userDetais override");
        return Optional.ofNullable(userFeignClient.findByEmail(userName).getBody())
                .orElseThrow( () -> new UsernameNotFoundException("User not found on database!"));
    }
}

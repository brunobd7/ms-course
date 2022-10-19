package com.dantas.hroauth.services;

import com.dantas.hroauth.entities.User;
import com.dantas.hroauth.feignclients.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    public User findByEmail(String email){
        logger.info("Call feign user -> search email");
        return Optional.ofNullable(userFeignClient.findByEmail(email).getBody())
                .orElseThrow( () -> new IllegalArgumentException("Email not found"));
    }
}

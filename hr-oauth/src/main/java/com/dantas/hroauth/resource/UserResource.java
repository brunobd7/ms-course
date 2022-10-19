package com.dantas.hroauth.resource;

import com.dantas.hroauth.entities.User;
import com.dantas.hroauth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger(UserResource.class);

    @GetMapping(value = "/search")
    public ResponseEntity<User> findByEmail(@RequestParam String email){

        try{
            User userFounded = userService.findByEmail(email);
            log.info("Email founded -> {}", userFounded.getEmail());
            return ResponseEntity.ok(userFounded);
        }catch (IllegalArgumentException exception){
            return ResponseEntity.notFound().build();
        }
    }
}


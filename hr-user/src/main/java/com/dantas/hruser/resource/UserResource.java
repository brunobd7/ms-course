package com.dantas.hruser.resource;

import com.dantas.hruser.entities.User;
import com.dantas.hruser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserRepository repository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<User>> findById(@PathVariable Long id){
        Optional<User> userFounded = repository.findById(id);
        return ResponseEntity.ok(userFounded);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<User> findByEmail(@RequestParam String email ){

        User userFounded = repository.findByEmail(email);
        return ResponseEntity.ok(userFounded);

    }

}

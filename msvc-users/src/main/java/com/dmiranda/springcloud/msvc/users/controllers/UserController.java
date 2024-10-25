package com.dmiranda.springcloud.msvc.users.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmiranda.springcloud.msvc.users.entities.User;
import com.dmiranda.springcloud.msvc.users.services.IUserService;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("User creando: {}", user);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        logger.info("User actualizando: {}", user);
        return userService.update(user, id).map(userUpdated -> ResponseEntity.status(HttpStatus.CREATED).body(userUpdated))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Llamada a método UserController::getUserById()");
        return userService.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("Llamada a método UserController::getUserByUsername()");
        return userService.findByUsername(username).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        logger.info("Llamada a método UserController::getAllUsers()");
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("User eliminando: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

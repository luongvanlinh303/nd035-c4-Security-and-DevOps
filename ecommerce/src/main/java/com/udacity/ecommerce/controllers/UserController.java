package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final static int PASSWORD_MINIMUM_SIZE = 7;
  Logger log = LogManager.getLogger(UserController.class);

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CartRepository cartRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      log.error("No user with this ID", new EntityNotFoundException());
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(user.get());
    }
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> findByUsername(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      log.error("No user with this Name", new EntityNotFoundException());
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(user);
    }
  }

  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
    User user = new User();
    Cart cart = new Cart();
    cartRepository.save(cart);

    user.setCart(cart);
    user.setUsername(createUserRequest.getUsername());
    log.info("Username set with {}", createUserRequest.getUsername());

    if (!meetsTheRequirements(createUserRequest.getPassword()) ||
        !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
      log.error("Error with user password. Can not create user {}", createUserRequest.getUsername());
      return ResponseEntity.badRequest().build();
    }

    user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
    userRepository.save(user);
    log.info("User created with username {}!", createUserRequest.getUsername());

    return ResponseEntity.ok(user);
  }

  private boolean meetsTheRequirements(String password) {
    return password != null && password.length() > PASSWORD_MINIMUM_SIZE;
  }
}

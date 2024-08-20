package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
  Logger log = LogManager.getLogger(CartController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ItemRepository itemRepository;

  @PostMapping("/addToCart")
  public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
    User user = userRepository.findByUsername(request.getUsername());
    if (user == null) {
      log.error("Error with username {}; it can not be found!", request.getUsername());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<Item> item = itemRepository.findById(request.getItemId());
    if (item.isEmpty()) {
      log.error("Error with item ID {}; it can not be found!", request.getItemId());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Cart cart = user.getCart();
    IntStream.range(0, request.getQuantity())
        .forEach(i -> cart.addItem(item.get()));
    cartRepository.save(cart);
    log.info("Item added to cart successfully! Username: {}", request.getUsername());
    return ResponseEntity.ok(cart);
  }

  @PostMapping("/removeFromCart")
  public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
    User user = userRepository.findByUsername(request.getUsername());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<Item> item = itemRepository.findById(request.getItemId());
    if (item.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Cart cart = user.getCart();
    IntStream.range(0, request.getQuantity())
        .forEach(i -> cart.removeItem(item.get()));
    cartRepository.save(cart);
    return ResponseEntity.ok(cart);
  }
}

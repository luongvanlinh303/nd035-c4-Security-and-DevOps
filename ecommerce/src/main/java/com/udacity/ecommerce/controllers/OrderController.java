package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.UserOrder;
import com.udacity.ecommerce.model.persistence.repositories.OrderRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
  Logger log = LogManager.getLogger(OrderController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;


  @PostMapping("/submit/{username}")
  public ResponseEntity<UserOrder> submit(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      log.error("Error with username {}; it can not be found!", username);
      return ResponseEntity.notFound().build();
    }

    UserOrder order = UserOrder.createFromCart(user.getCart());
    orderRepository.save(order);

    log.info("Order submitted successfully! Recipient username: {}", username);
    return ResponseEntity.ok(order);
  }

  @GetMapping("/history/{username}")
  public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(orderRepository.findByUser(user));
  }
}

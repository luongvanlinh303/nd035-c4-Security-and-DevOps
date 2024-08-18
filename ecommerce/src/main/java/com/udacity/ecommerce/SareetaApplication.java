package com.udacity.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EntityScan("com.udacity.ecommerce.model.persistence")
@EnableJpaRepositories("com.udacity.ecommerce.model.persistence.repositories")
public class SareetaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SareetaApplication.class, args);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoderBean() {
    return new BCryptPasswordEncoder();
  }
}

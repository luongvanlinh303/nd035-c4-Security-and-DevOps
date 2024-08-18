package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findByName(String name);

}

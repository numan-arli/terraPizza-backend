package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.CartItem;
import com.terra.terraPizza.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}
package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.Order;
import com.terra.terraPizza.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);

    List<Order> findByUser(User user);
    Optional<Order> findByConversationId(String conversationId);

}

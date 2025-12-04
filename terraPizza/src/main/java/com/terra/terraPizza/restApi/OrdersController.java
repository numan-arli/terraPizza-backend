package com.terra.terraPizza.restApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terra.terraPizza.Bussines.IPizzaService;
import com.terra.terraPizza.Bussines.OrderMapper;
import com.terra.terraPizza.DataAcces.*;
import com.terra.terraPizza.Entities.*;
import com.terra.terraPizza.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RestController
public class OrdersController {

    private final IPizzaService pizzaService;

    @Autowired
    public OrdersController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    // 🔹 Kullanıcının siparişlerini listele
    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return pizzaService.getOrders();
    }


    // 🔹 Yeni sipariş oluştur
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        return pizzaService.createOrder(request);
    }

}

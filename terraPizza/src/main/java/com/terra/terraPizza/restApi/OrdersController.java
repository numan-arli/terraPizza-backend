package com.terra.terraPizza.restApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderMapper orderMapper;
    @Autowired private ProductRepository productRepository;

    // 🔹 Kullanıcının siparişlerini listele
    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getItems().stream()
                                .map(OrderItemResponse::new)
                                .toList()
                ))
                .toList();
    }







    // 🔹 Yeni sipariş oluştur
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Hazırlanıyor");
        order.setPaymentStatus(2); // string veya int yapına göre
        order.setTotalPrice(request.getTotalPrice());

        List<OrderItem> items = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(); // JSON dönüşümü için

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ürün bulunamadı"));

            OrderItem item = new OrderItem();
            item.setOrder(order); // parent ile ilişkilendir
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setDescription(product.getDescription());

            BigDecimal totalPrice = itemReq.getTotalPrice() != null
                    ? itemReq.getTotalPrice()
                    : product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            item.setTotalPrice(totalPrice);

            // 🔹 Toppings JSON olarak kaydediliyor
            try {
                if (itemReq.getToppings() != null) {
                    item.setToppings(mapper.writeValueAsString(itemReq.getToppings()));
                } else {
                    item.setToppings("[]");
                }
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Toppings JSON dönüşümü hatası");
            }

            // 🔹 Extra Mozzarella alanı
            item.setExtraMozzarella(itemReq.isExtraMozzarella());

            items.add(item);
        }

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", savedOrder.getId());
        response.put("status", "created");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}

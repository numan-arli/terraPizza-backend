package com.terra.terraPizza.restApi;

import com.iyzipay.model.Payment;
import com.terra.terraPizza.Bussines.*;
import com.terra.terraPizza.DataAcces.OrderRepository;
import com.terra.terraPizza.Entities.CardInfoDto;
import com.terra.terraPizza.Entities.Order;
import com.terra.terraPizza.Entities.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
 @PostMapping("/non3d/{orderId}")
    public ResponseEntity<?> payNon3D(@PathVariable Long orderId, @RequestBody CardInfoDto cardInfo) {

     Payment payment = paymentService.createPayment(orderId, cardInfo); // Payment nesnesi al

     Map<String, Object> result = new HashMap<>();
     result.put("status", payment.getStatus());

     if (payment.getErrorMessage() != null) {
         result.put("errorMessage", payment.getErrorMessage());
     }



     if ("success".equalsIgnoreCase((String) result.get("status"))) {
            Order order = orderRepository.findById(orderId).orElseThrow();
            order.setPaymentStatus(1);
            order.setConversationId(payment.getConversationId());
            order.setStatus("ödeme alındı");
            orderRepository.save(order);
            return ResponseEntity.ok(Map.of("status", "success"));
        } else {
         System.out.println("errorCode:" + payment.getErrorCode() + " " + "errorMessage: " + payment.getErrorMessage());
            return ResponseEntity.status(402).body(Map.of(
                    "status", "failed",
                    "error", result.get("errorMessage")
            ));
        }
    }
}


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

    private final IPaymentService paymentService;
    private final OrderRepository orderRepository;

 @PostMapping("/non3d/{orderId}")
     public ResponseEntity<?> payNon3D(@PathVariable Long orderId, @RequestBody CardInfoDto cardInfo) {

     Payment payment = paymentService.createPayment(orderId, cardInfo); // Payment nesnesi al
     return paymentService.paymentstatus(orderId, payment);
    }
}


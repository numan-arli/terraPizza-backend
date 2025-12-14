package com.terra.terraPizza.Bussines;

import com.iyzipay.Options;
import com.iyzipay.model.Payment;
import com.terra.terraPizza.Entities.CardInfoDto;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    Payment createPayment(Long orderId, CardInfoDto cardInfoDto);
    Options getOptions();
    ResponseEntity<?> paymentstatus(Long orderId,Payment payment);
}

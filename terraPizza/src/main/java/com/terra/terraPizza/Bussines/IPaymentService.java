package com.terra.terraPizza.Bussines;

import com.terra.terraPizza.Entities.CardInfoDto;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<?> payNon3D(Long orderId, CardInfoDto cardInfo);
}

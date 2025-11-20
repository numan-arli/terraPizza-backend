package com.terra.terraPizza.DataAcces;

import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.Buyer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentRequestDto {
    private BigDecimal price;
    private BigDecimal paidPrice;
    private int installment;
    private Buyer buyer;
    private Address shippingAddress;
    private Address billingAddress;
    private List<BasketItem> basketItems;
}
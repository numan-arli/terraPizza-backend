package com.terra.terraPizza.Bussines;

import com.iyzipay.Options;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Currency;
import java.util.UUID;
import com.iyzipay.request.CreatePaymentRequest;
import com.terra.terraPizza.DataAcces.OrderRepository;
import com.terra.terraPizza.Entities.CardInfoDto;
import com.terra.terraPizza.Entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }



    private Options getOptions() {
        Options options = new Options();
        options.setApiKey("sandbox-kBxcYm3MrhR3pB2yQKog1AENWuICGL9v"); // kendi key'inle değiştir
        options.setSecretKey("sandbox-zjkuKN107rCi5RBVQbb6nLDTweQsr2Z1");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");
        return options;
    }

    public Payment createPayment(Long orderId, CardInfoDto cardInfoDto) {
        Options options = getOptions();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.get();


        // Kart bilgisi
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(cardInfoDto.cardHolderName);
        paymentCard.setCardNumber(cardInfoDto.getCardNumber());
        paymentCard.setExpireMonth(cardInfoDto.getExpireMonth());
        paymentCard.setExpireYear(cardInfoDto.getExpireYear());
        paymentCard.setCvc(cardInfoDto.getCvc());
        paymentCard.setRegisterCard(0);

        // Alıcı bilgisi
        Buyer buyer = new Buyer();
        buyer.setId("BY789");
        buyer.setName(order.getUser().getFirstName());
        buyer.setSurname(order.getUser().getLastName());
        buyer.setEmail(order.getUser().getEmail());
        buyer.setIdentityNumber("74300864791");
        buyer.setRegistrationAddress("Istanbul Turkey");
        buyer.setIp("85.34.78.112");
        buyer.setCountry("Turkey");
        buyer.setCity("samsun");

        // Adres
        Address shippingAddress = new Address();
        shippingAddress.setContactName(order.getUser().getFirstName());
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Istanbul Turkey");

        Address billingAddress = shippingAddress;

        // Sepet
        BasketItem item = new BasketItem();
        item.setId(String.valueOf(order.getId()));
        item.setName("pizza");
        item.setCategory1("Food");
        item.setItemType(BasketItemType.PHYSICAL.name());
        item.setPrice(order.getTotalPrice());

        List<BasketItem> basketItems = new ArrayList<>();
        basketItems.add(item);

        // 🔹 Asıl request nesnesi bu olmalı
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setLocale("tr");
        request.setConversationId(UUID.randomUUID().toString());
        request.setPrice(order.getTotalPrice());
        request.setPaidPrice(order.getTotalPrice());
        request.setCurrency("TRY");
        request.setInstallment(1);
        request.setBasketId("B67832");
        request.setPaymentChannel("WEB");
        request.setPaymentGroup("PRODUCT");
        request.setPaymentCard(paymentCard);
        request.setBuyer(buyer);
        request.setShippingAddress(shippingAddress);
        request.setBillingAddress(billingAddress);
        request.setBasketItems(basketItems);

        // Ödemeyi oluştur
        return Payment.create(request, options);
    }
}

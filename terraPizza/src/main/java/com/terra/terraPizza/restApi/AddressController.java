package com.terra.terraPizza.restApi;

import com.terra.terraPizza.Bussines.IPizzaService;
import com.terra.terraPizza.Bussines.PizzaService;
import com.terra.terraPizza.DataAcces.AddressRepository;
import com.terra.terraPizza.DataAcces.AddressRequest;
import com.terra.terraPizza.Entities.Address;
import com.terra.terraPizza.Entities.User;
import com.terra.terraPizza.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final IPizzaService pizzaService;

    @Autowired
    public AddressController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody AddressRequest request) {
        return pizzaService.addAddress(request);
    }


    @GetMapping
    public ResponseEntity<?> getUserAddresses() {
        return pizzaService.getUserAddresses();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        return pizzaService.deleteAddress(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
       return pizzaService.updateAddress(id,request);
    }


    @PatchMapping("/{id}/default") // veya @PatchMapping da kullanabilirsin
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long id) {
        return pizzaService.setDefaultAddress(id);
    }

}


package com.terra.terraPizza.restApi;

import com.terra.terraPizza.Bussines.IPizzaService;
import com.terra.terraPizza.DataAcces.PasswordChangeRequest;
import com.terra.terraPizza.DataAcces.UserProfileUpdateRequest;
import com.terra.terraPizza.Entities.User;
import com.terra.terraPizza.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final IPizzaService pizzaService;

    @Autowired
    public ProfileController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    // ✅ Kullanıcı bilgilerini çekmek için GET
    @GetMapping
    public ResponseEntity<?> getProfile() {
        return pizzaService.getProfile();
    }


    // ✅ Kullanıcı bilgilerini güncellemek için PUT
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return pizzaService.updateProfile(request);
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        return pizzaService.changePassword(request);
    }


}

package com.terra.terraPizza.restApi;

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

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody AddressRequest request) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        Address address = new Address();
        address.setTitle(request.getTitle());
        address.setFullAddress(request.getFullAddress());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setUser(user);

        addressRepository.save(address);

        return ResponseEntity.ok("Adres başarıyla eklendi");
    }

    @GetMapping
    public ResponseEntity<?> getUserAddresses() {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        List<Address> addresses = addressRepository.findByUser(user);

        return ResponseEntity.ok(addresses);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres bulunamadı");
        }

        Address address = optionalAddress.get();
        String email = getCurrentEmail();
        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        addressRepository.delete(address);
        return ResponseEntity.ok("Adres silindi");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres bulunamadı");
        }

        Address address = optionalAddress.get();
        String email = getCurrentEmail();
        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        address.setTitle(request.getTitle());
        address.setFullAddress(request.getFullAddress());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());

        addressRepository.save(address);
        return ResponseEntity.ok("Adres güncellendi");
    }


    @PatchMapping("/{id}/default") // veya @PatchMapping da kullanabilirsin
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long id) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        List<Address> addresses = addressRepository.findByUser(user);
        addresses.forEach(a -> {
            a.setDefault(false);
            addressRepository.save(a);
        });

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı"));

        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        address.setDefault(true);
        addressRepository.save(address);

        return ResponseEntity.ok("Varsayılan adres olarak ayarlandı");
    }





    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}


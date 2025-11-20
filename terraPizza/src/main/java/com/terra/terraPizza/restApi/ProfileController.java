package com.terra.terraPizza.restApi;

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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Kullanıcı bilgilerini çekmek için GET
    @GetMapping
    public ResponseEntity<?> getProfile() {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }


    // ✅ Kullanıcı bilgilerini güncellemek için PUT
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());
        user.setSms(request.isSms());
        user.setEmailPermission(request.isEmailPermission());
        user.setPhonePermission(request.isPhonePermission());

        userRepository.save(user);

        return ResponseEntity.ok("Profil güncellendi");
    }

    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // username yerine email dönüyorsa burası email olur
        } else {
            return principal.toString();
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        String email = getCurrentEmail(); // JWT'den email al
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        // Eski şifreyi kontrol et
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eski şifre hatalı");
        }

        // Yeni şifreyi encode edip kaydet
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Şifre başarıyla değiştirildi");
    }


}

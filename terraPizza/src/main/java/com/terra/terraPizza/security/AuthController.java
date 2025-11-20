
package com.terra.terraPizza.security;
import com.terra.terraPizza.Entities.Role;
import com.terra.terraPizza.Entities.User;
import com.terra.terraPizza.security.AuthRequest;
import com.terra.terraPizza.security.AuthResponse;
import com.terra.terraPizza.security.RegisterRequest;
import com.terra.terraPizza.security.JwtUtil;
import com.terra.terraPizza.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "email already in use"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            String roleName = request.getRole() != null ? request.getRole() : "USER";
            user.setRole(Role.valueOf(roleName));

        } catch (IllegalArgumentException e) {
            return  ResponseEntity.badRequest().body(Map.of("error", "Invalid role"));

        }

        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), List.of(user.getRole()));
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFirstName(),user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), List.of(user.getRole()));
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFirstName(), user.getRole()));
    }

}

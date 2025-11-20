package com.terra.terraPizza.restApi;

import com.terra.terraPizza.DataAcces.FavoriteRepository;
import com.terra.terraPizza.DataAcces.FavoriteRequest;
import com.terra.terraPizza.DataAcces.ProductRepository;
import com.terra.terraPizza.Entities.Favorite;
import com.terra.terraPizza.Entities.Product;
import com.terra.terraPizza.Entities.User;
import com.terra.terraPizza.security.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest request) {
        String email = getCurrentEmail();
       // User user = userRepository.findByEmail(email).orElseThrow();
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        //if (favoriteRepository.existsByUserAndProduct(user, product)) {
         //   return ResponseEntity.status(HttpStatus.CONFLICT).body("Zaten favorilerde");
       // }

        Optional<Favorite> existing = favoriteRepository.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Zaten favorilerde");
        }


        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);

        return ResponseEntity.ok("Favoriye eklendi");
    }



    @Transactional
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long productId) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow();

        favoriteRepository.deleteByUserAndProduct(user, product);
        return ResponseEntity.ok("Favoriden çıkarıldı");
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

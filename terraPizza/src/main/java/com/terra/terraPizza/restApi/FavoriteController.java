package com.terra.terraPizza.restApi;

import com.terra.terraPizza.Bussines.IPizzaService;
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

    private final IPizzaService pizzaService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public FavoriteController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest request) {
        return pizzaService.addFavorite(request);
    }


    @Transactional
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long productId) {
        return pizzaService.removeFavorite(productId);
    }

}

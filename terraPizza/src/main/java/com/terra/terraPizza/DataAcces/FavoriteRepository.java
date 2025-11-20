package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.Favorite;
import com.terra.terraPizza.Entities.Product;
import com.terra.terraPizza.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    Optional<Favorite> findByUserAndProduct(User user, Product product);

}

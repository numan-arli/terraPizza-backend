package com.terra.terraPizza.Bussines;

import com.terra.terraPizza.DataAcces.*;
import com.terra.terraPizza.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IPizzaService{
    ResponseEntity<?> addAddress(AddressRequest request);
    ResponseEntity<?> getUserAddresses();
    ResponseEntity<?> deleteAddress( Long id);
    ResponseEntity<?> updateAddress(Long id, AddressRequest request);
    ResponseEntity<?> setDefaultAddress(Long id);
    ResponseEntity<List<Branch>> getAllBranches();
    ResponseEntity<Branch> createBranch(Branch branch);
    ResponseEntity<?> updateBranch(Long id, Branch updatedBranch);
    ResponseEntity<?> deleteBranch(Long id);
    ResponseEntity<List<Branch>> searchBranches(
            String city,
            String district,
            String neighborhood
    );
    ResponseEntity<Map<String, List<Map<String, String>>>> getFilterOptions();
    ResponseEntity<List<Branch>> getAllBranches2();
    ResponseEntity<?> addFavorite(FavoriteRequest request);
    ResponseEntity<?> removeFavorite(Long productId);
    List<OrderResponse> getOrders();
    ResponseEntity<?> createOrder(OrderRequest request);
    List<PizzaDto> getAllPizza();
    List<Product> getByCategory(String category);
    ResponseEntity<?> getProfile();
    ResponseEntity<?> updateProfile(UserProfileUpdateRequest request);
    ResponseEntity<?> changePassword(PasswordChangeRequest request);

}

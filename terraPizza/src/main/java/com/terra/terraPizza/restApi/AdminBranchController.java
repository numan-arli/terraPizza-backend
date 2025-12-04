package com.terra.terraPizza.restApi;

import com.terra.terraPizza.Bussines.IPizzaService;
import com.terra.terraPizza.DataAcces.BranchRepository;
import com.terra.terraPizza.Entities.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/branches")
//@PreAuthorize("hasRole('ADMIN')") // sadece admin erişebilir
public class AdminBranchController {

    private final IPizzaService pizzaService;

    @Autowired
    public AdminBranchController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    // 🔹 Tüm şubeleri listele
    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return pizzaService.getAllBranches();
    }

    // 🔹 Yeni şube ekle
    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        return pizzaService.createBranch(branch);
    }

    // 🔹 Şube güncelle
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody Branch updatedBranch) {
        return pizzaService.updateBranch(id,updatedBranch);
    }

    // 🔹 Şube sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        return pizzaService.deleteBranch(id);
    }
}

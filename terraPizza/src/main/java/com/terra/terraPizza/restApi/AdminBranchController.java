package com.terra.terraPizza.restApi;

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

    @Autowired
    private BranchRepository branchRepo;

    // 🔹 Tüm şubeleri listele
    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchRepo.findAll());
    }

    // 🔹 Yeni şube ekle
    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch saved = branchRepo.save(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 🔹 Şube güncelle
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody Branch updatedBranch) {
        return branchRepo.findById(id)
                .map(branch -> {
                    branch.setCity(updatedBranch.getCity());
                    branch.setDistrict(updatedBranch.getDistrict());
                    branch.setNeighborhood(updatedBranch.getNeighborhood());
                    branch.setName(updatedBranch.getName());
                    branch.setOpen(updatedBranch.isOpen());
                    branch.setHours(updatedBranch.getHours());
                    branch.setLatitude(updatedBranch.getLatitude());
                    branch.setLongitude(updatedBranch.getLongitude());
                    branchRepo.save(branch);
                    return ResponseEntity.ok(branch);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Şube sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        if (!branchRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Şube bulunamadı");
        }
        branchRepo.deleteById(id);
        return ResponseEntity.ok("Şube silindi");
    }
}

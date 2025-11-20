package com.terra.terraPizza.restApi;

import com.terra.terraPizza.Bussines.IPizzaService;
import com.terra.terraPizza.DataAcces.BranchRepository;
import com.terra.terraPizza.Entities.Branch;
import com.terra.terraPizza.Entities.PizzaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RestController
public class BranchController {


    @Autowired
    private BranchRepository branchRepo;

    @GetMapping("/branches/search")
    public ResponseEntity<List<Branch>> searchBranches(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String neighborhood
    ) {
        List<Branch> results = branchRepo.findByCityAndDistrictAndNeighborhood(city, district, neighborhood);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/branches/filters")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getFilterOptions() {
        List<String> cities = branchRepo.findAll().stream()
                .map(Branch::getCity)
                .distinct()
                .toList();

        List<Map<String, String>> districts = branchRepo.findAll().stream()
                .map(b -> Map.<String, String>of(
                        "name", b.getDistrict(),
                        "city", b.getCity()
                ))
                .distinct()
                .toList();

        List<Map<String, String>> neighborhoods = branchRepo.findAll().stream()
                .map(b -> Map.<String, String>of(
                        "name", b.getNeighborhood(),
                        "district", b.getDistrict()
                ))
                .distinct()
                .toList();

        Map<String, List<Map<String, String>>> result = Map.of(
                "cities", cities.stream().map(c -> Map.<String,String>of("name", c)).toList(), // city'yi de Map yapıyoruz
                "districts", districts,
                "neighborhoods", neighborhoods
        );

        return ResponseEntity.ok(result);
    }



    @GetMapping("/branches/list")
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchRepo.findAll());
    }
}

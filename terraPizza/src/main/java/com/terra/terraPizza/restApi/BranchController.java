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

    private final IPizzaService pizzaService;

    @Autowired
    public BranchController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }


    @GetMapping("/branches/search")
    public ResponseEntity<List<Branch>> searchBranches(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String neighborhood
    ) {

        return pizzaService.searchBranches(city,district,neighborhood);
    }

    @GetMapping("/branches/filters")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getFilterOptions() {
        return pizzaService.getFilterOptions();
    }


    @GetMapping("/branches/list")
    public ResponseEntity<List<Branch>> getAllBranches2() {
        return pizzaService.getAllBranches2();
    }
}

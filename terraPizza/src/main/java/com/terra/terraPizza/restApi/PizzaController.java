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
public class PizzaController {

    private final IPizzaService pizzaService;

    @Autowired
    private BranchRepository branchRepo;

    public PizzaController(IPizzaService pizzaService){
        this.pizzaService = pizzaService;
    }

    @GetMapping("/allPizza")
    public List<PizzaDto> getAllPizza(){
       return pizzaService.findAll()
               .stream().map(PizzaDto::new)
               .collect(Collectors.toList());
    }


}

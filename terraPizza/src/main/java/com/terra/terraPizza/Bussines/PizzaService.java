package com.terra.terraPizza.Bussines;

import com.terra.terraPizza.DataAcces.PizzaRepository;
import com.terra.terraPizza.Entities.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public abstract class PizzaService implements IPizzaService {
}

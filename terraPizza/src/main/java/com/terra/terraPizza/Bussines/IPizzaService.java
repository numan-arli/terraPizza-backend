package com.terra.terraPizza.Bussines;

import com.terra.terraPizza.Entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPizzaService extends JpaRepository<Pizza,Integer> {
}

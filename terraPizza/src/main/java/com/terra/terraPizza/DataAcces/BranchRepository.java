package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByCityAndDistrictAndNeighborhood(String city, String district, String neighborhood);
}


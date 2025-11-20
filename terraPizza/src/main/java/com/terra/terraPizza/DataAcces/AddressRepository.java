package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.Address;
import com.terra.terraPizza.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}


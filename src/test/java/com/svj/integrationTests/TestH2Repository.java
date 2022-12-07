package com.svj.integrationTests;

import com.svj.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<CustomerEntity, Integer> {
}

package com.dits.dits_group.service.repository;

import com.dits.dits_group.service.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByActiveTrue();
}
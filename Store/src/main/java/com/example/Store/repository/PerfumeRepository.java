package com.example.Store.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Store.model.Perfume;

@Repository
public interface PerfumeRepository
extends JpaRepository<Perfume, Long> {

    List<Perfume>
    findByNameContainingIgnoreCase(String name);

    List<Perfume>
    findByQuantityGreaterThan(Integer quantity);
}
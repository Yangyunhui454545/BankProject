package com.example.testproject.repository;

import com.example.testproject.entity.OrdrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrdrEntity, Integer> {
}

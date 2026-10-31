package com.ratesrisk.repository;

import com.ratesrisk.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> { }

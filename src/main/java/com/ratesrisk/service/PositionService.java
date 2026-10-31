package com.ratesrisk.service;

import com.ratesrisk.domain.Position;
import com.ratesrisk.repository.PositionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PositionService {
    private final PositionRepository repository;
    public PositionService(PositionRepository repository) { this.repository = repository; }
    public Position create(Position position) { return repository.save(position); }
    public List<Position> all() { return repository.findAll(); }
}

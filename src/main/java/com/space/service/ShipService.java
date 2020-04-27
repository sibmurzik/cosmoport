package com.space.service;


import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShipService {
    @Autowired
    ShipRepository repo;

    public void save(Ship ship) {
        repo.save(ship);
    }

    public List<Ship> listAll() {
        return (List<Ship>) repo.findAll();
    }

    public Ship get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}

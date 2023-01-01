package com.selt.service;

import com.selt.model.Department;
import com.selt.model.Location;
import com.selt.repository.LocationRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class LocationService {

    private final LocationRepo locationRepo;


    public void save(Location location) {
        locationRepo.save(location);
    }

    public void delete(Location location) {
        locationRepo.delete(location);
    }

    public List<Location> findAll() {
        return locationRepo.findAll();
    }


    public void deleteLocation(long id) {
        Optional<Location> location = locationRepo.findById(id);
       locationRepo.delete(location.get());
    }

    public List<Location> findAllByNameOfLocationIsLike(String name){
        return locationRepo.findAllByNameOfLocationIsLike(name);
    }

}

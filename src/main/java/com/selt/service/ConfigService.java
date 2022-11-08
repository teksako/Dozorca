package com.selt.service;

import com.selt.model.Configuration;
import com.selt.repository.ConfigRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Data
public class ConfigService {

    private final ConfigRepo configRepo;

    public void save(Configuration config){
        configRepo.save(config);
    }
    public Optional<Configuration> findById(){
        return configRepo.findById(1l);
    }
    public List<Configuration> findAll(){
        return configRepo.findAll();
    }


}

package com.selt.service;

import com.selt.model.Toner;
import com.selt.repository.TonerRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class TonerService {

    private final TonerRepo tonerRepo;
    private static Toner toner = new Toner();
    private static List<Toner> tonerList = new ArrayList<>();

    public void save(Toner toner) {

        tonerRepo.save(toner);

    }

    public List<Toner> findAllByTonerNameIsLike(String name){
        return tonerRepo.findAllByTonerNameIsLike(name);
    }

    public Optional<Toner> findById(Long id){
        return tonerRepo.findById(id);
    }
    public void delete(Toner toner) {
        tonerRepo.delete(toner);

    }
    public void deleteToner(long id) {
        Optional<Toner> toner = tonerRepo.findById(id);
        tonerRepo.delete(toner.get());
    }

    public void update(Toner toner) {
        Optional<Toner> toner1 = tonerRepo.findById(toner.getId());
        toner1.get().setTonerName(toner.getTonerName());
        tonerRepo.save(toner1.get());

    }

    public List<Toner> findAll() {
        return tonerRepo.findAll();
    }


}

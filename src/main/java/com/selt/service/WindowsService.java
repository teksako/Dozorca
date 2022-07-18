package com.selt.service;

import com.selt.model.Windows;
import com.selt.repository.WindowsRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class WindowsService {

    private final WindowsRepo windowsRepo;


    public void save(Windows windows) {
        windowsRepo.save(windows);
    }

    public void delete(Windows windows) {
        windowsRepo.delete(windows);
    }

    public List<Windows> findAll() {
        return windowsRepo.findAll();
    }
}

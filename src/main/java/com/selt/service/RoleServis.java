package com.selt.service;

import com.selt.model.Role;
import com.selt.model.UserRole;
import com.selt.repository.RoleRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class RoleServis {

    private final RoleRepo roleRepo;

    public List<UserRole> findAll() {
        return roleRepo.findAll();
    }
}

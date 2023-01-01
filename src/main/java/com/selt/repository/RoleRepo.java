package com.selt.repository;

import com.selt.model.Role;
import com.selt.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<UserRole, Long> {

}

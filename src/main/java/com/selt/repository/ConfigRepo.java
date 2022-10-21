package com.selt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.servlet.jsp.jstl.core.Config;


public interface ConfigRepo extends JpaRepository<Config, Long> {
}

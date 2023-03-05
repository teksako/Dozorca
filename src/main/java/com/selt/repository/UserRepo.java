package com.selt.repository;

import com.selt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByLogin(String username);
    List<User> findAllByFullnameIsLike(String fullname);
    List<User> findAllByLoginIsLike(String login);

}

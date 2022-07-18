package com.selt.service;

import com.selt.model.Printer;
import com.selt.model.User;
import com.selt.repository.UserRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
@Transactional
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

//    public List<User> findAll() {
//        return userRepo.findAllByUsername("admin");
//    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public void save(@NotNull User user, String password) {
       // if (userRepo.findUserByLogin(user.getLogin()) == null) {
            user.setCreateDate(new Date());
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
//            return "Utworzyłeś uzytkownika: " + user.getLogin();
//        } else {
//            return "Taki użytkownik istnieje!";
//        }
    }

    public void saveUpdate(@NotNull User user, String password){
        Date date;
        date = user.getCreateDate();
        user.setCreateDate(date);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    private UserDetails userDetailsService() {

        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void changePassword(String password) {
        Optional<User> actualUser = Optional.ofNullable(userRepo.findUserByLogin(userDetailsService().getUsername()));
        //Optional<User> user1 = userRepo.findById(userDetailsService(). getId());
        if (actualUser.isPresent()) {
            User user = actualUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
        }
    }

    public void resetPasswordByAdmin(User user, String password) {
        Optional<User> user1 = userRepo.findById(user.getId());
        if (user1.isPresent()) {
            User userToChange = user1.get();
            userToChange.setPassword(passwordEncoder.encode(password));
            userRepo.save(userToChange);
        }
    }

    public String actualLoginUser() {
        return userDetailsService().getUsername();
    }

    public User findUserByUsername() {
        return userRepo.findUserByLogin(actualLoginUser());
    }

    public void deleteUser(long id) {
        Optional<User> user = userRepo.findById(id);
        userRepo.delete(user.get());
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public List<User> findAllByFullNameIsLike(String fullname){
        return userRepo.findAllByFullnameIsLike(fullname);
    }
}

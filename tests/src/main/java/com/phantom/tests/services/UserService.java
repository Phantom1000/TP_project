package com.phantom.tests.services;

import com.phantom.tests.models.Role;
import com.phantom.tests.models.User;
import com.phantom.tests.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getSurname() + user.getFirstname() + user.getPatronymic());
        if (userFromDb != null) {
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setUsername(user.getSurname() + user.getFirstname() + user.getPatronymic());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public List<User> search(String pattern) {
        return userRepo.search(pattern);
    }

    public void updateUser(User user, User newUser) {
        String surname = newUser.getSurname();
        String firstname = newUser.getFirstname();
        String patronymic = newUser.getPatronymic();
        String password = newUser.getPassword();
        boolean isSurnameChanged = (user.getSurname() != null && !user.getSurname().equals(surname) || (surname != null && !surname.equals(user.getSurname())));
        boolean isFirstnameChanged = (user.getFirstname() != null && !user.getFirstname().equals(firstname) || (firstname != null && !firstname.equals(user.getFirstname())));
        boolean isPatronymicChanged = (user.getPatronymic() != null && !user.getPatronymic().equals(patronymic) || (patronymic != null && !patronymic.equals(user.getPatronymic())));
        if (isSurnameChanged) {
            user.setSurname(surname);
        }

        if (isFirstnameChanged) {
            user.setFirstname(firstname);
        }

        if (isPatronymicChanged) {
            user.setPatronymic(patronymic);
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (isSurnameChanged || isFirstnameChanged || isPatronymicChanged) {
            user.setUsername(surname + firstname + patronymic);
        }

        userRepo.save(user);
    }

}

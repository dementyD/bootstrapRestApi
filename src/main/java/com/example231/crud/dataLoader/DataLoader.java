package com.example231.crud.dataLoader;

import com.example231.crud.model.Role;
import com.example231.crud.model.User;
import com.example231.crud.repositories.RoleRepository;
import com.example231.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void run(ApplicationArguments args) {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.saveAll(Arrays.asList(admin, user));
        }
        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setLastname("admin");
        userAdmin.setAge(222);
        userAdmin.setEmail("admin@mail.ru");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userAdmin.setPassword(encoder.encode("admin"));
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(admin);
        roleSet.add(user);
        userAdmin.setRoles(roleSet);
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(userAdmin);
        }
    }
}
package com.example231.crud.service;

import com.example231.crud.model.Role;
import com.example231.crud.model.User;
import com.example231.crud.repositories.RoleRepository;
import com.example231.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean adminAddedUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setRoles(installRole(user));
        cryptPassword(user);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean addDefaultUser(User user) {
        cryptPassword(user);

        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        Role roleUser = roleRepository.findByRole("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleUser);
        user.setRoles(roleSet);
        userRepository.save(user);
        return true;
    }

    @Override
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public void updateUser(Long id, User user) {
        User userUpdate = getUserById(id);
        userUpdate.setUsername(user.getUsername());
        userUpdate.setLastname(user.getLastname());
        userUpdate.setAge(user.getAge());
        userUpdate.setEmail(user.getEmail());
        if (!user.getPassword().equals("")) {
            userUpdate.setPassword(user.getPassword());
            cryptPassword(userUpdate);
        }
        if (user.getRoles().iterator().next() != null) {
            userUpdate.setRoles(installRole(user));
        }
        userRepository.save(userUpdate);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUserByName(s);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void cryptPassword(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
    }

    public Set<Role> installRole(User user) {
        Role role = user.getRoles().stream().findFirst().get();
        Set<Role> roleSet = new HashSet<>();
        if (role.getRole().equals("ROLE_ADMIN")) {
            roleSet.add(new Role(1l, "ROLE_ADMIN"));
        } else {
            roleSet.add(new Role(2l, "ROLE_USER"));
        }
        return roleSet;
    }

}


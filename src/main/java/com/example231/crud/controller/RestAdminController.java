package com.example231.crud.controller;

import com.example231.crud.model.User;
import com.example231.crud.repositories.RoleRepository;
import com.example231.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class RestAdminController {
    private UserService userService;
    private RoleRepository roleRepository;

    @Autowired
    public RestAdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/adminGetAll")
    public List<User> getAllUser() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUserSession")
    public User getUserSession(Principal principal) {
        return userService.getUserByName(principal.getName());
    }

    @PostMapping("/admin")
    public void newUser(@RequestBody User user) {
        userService.adminAddedUser(user);
    }

    @PutMapping("/admin/edit")
    public void editUser(@RequestBody User user) {
        userService.updateUser(user.getId(), user);
    }

    @DeleteMapping("/admin/delete")
    public void deleteUser(@RequestBody User user) {
        userService.removeUser(user.getId());
    }

}

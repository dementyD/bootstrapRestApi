package com.example231.crud.controller;

import com.example231.crud.model.User;
import com.example231.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RestUsersController {
    private UserService userService;

    @Autowired
    public RestUsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public User getUser(Principal principal) {
        return userService.getUserByName(principal.getName());
    }

}




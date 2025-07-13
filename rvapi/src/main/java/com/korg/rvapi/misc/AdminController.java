package com.korg.rvapi.misc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korg.rvapi.user.UserRepository;
import com.korg.rvapi.user.UserTable;

//todo: remove
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    // read all
    @GetMapping("/users")
    public List<UserTable> getAllUsers() {
        return userRepository.findAll();
    }
}

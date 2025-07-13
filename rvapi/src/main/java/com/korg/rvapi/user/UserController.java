package com.korg.rvapi.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserTable> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserTable getUserById(@PathVariable String id) {
        return userRepository.findById(id).get();
    }

    @PostMapping
    public UserTable createUser(@RequestBody UserCreate user) {
        UserTable userEntry = new UserTable();
        String userId = UUID.randomUUID().toString();

        userEntry.setId(userId);
        userEntry.setName(user.getName());
        userEntry.setEmail(user.getEmail());
        userEntry.setHashpw(user.getPassword());

        return userRepository.save(userEntry);
    }

    @PutMapping("/{id}")
    public UserTable updateUser(@PathVariable String id, @RequestBody UserTable user) {
        UserTable existingUser = userRepository.findById(id).get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        try {
            userRepository.findById(id).get();
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }
}

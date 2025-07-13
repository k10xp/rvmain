package com.korg.rvapi.user;

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

import com.korg.rvapi.auth.JwtLogic;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtLogic jwtLogic;

    // read single
    @GetMapping("/{id}")
    public UserTable getUserById(@PathVariable String id) {
        return userRepository.findById(id).get();
    }

    // create new user
    @PostMapping
    public UserTable createUser(@RequestBody UserCreate user) {
        UserTable userEntry = new UserTable();

        String userId = UUID.randomUUID().toString();
        String token = jwtLogic.generateToken(userId, user.getEmail());

        userEntry.setId(userId);
        userEntry.setName(user.getName());
        userEntry.setEmail(user.getEmail());
        userEntry.setUsername(user.getUsername());
        userEntry.setHashpw(user.getPassword());
        userEntry.setJwt(token);

        return userRepository.save(userEntry);
    }

    // update
    @PutMapping("/{id}")
    public UserTable updateUser(@PathVariable String id, @RequestBody UserCreate user) {
        UserTable existingUser = userRepository.findById(id).get();

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setHashpw(user.getPassword());

        // create new jwt
        String token = jwtLogic.generateToken(existingUser.getId(), user.getEmail());
        existingUser.setJwt(token);

        return userRepository.save(existingUser);
    }

    // delete
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

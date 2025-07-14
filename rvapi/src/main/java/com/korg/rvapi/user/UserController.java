package com.korg.rvapi.user;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.korg.rvapi.auth.JwtLogic;
import com.korg.rvapi.auth.PwHash;
import com.korg.rvapi.errors.DuplicateUserException;
import com.korg.rvapi.user.models.Login;
import com.korg.rvapi.user.models.UserCreate;
import com.korg.rvapi.user.models.UserResponse;
import com.korg.rvapi.user.models.UserTable;

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
    public UserResponse createUser(@RequestBody UserCreate user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException("Email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException("Username already exists");
        }

        UserTable userEntry = new UserTable();

        String userId = UUID.randomUUID().toString();

        userEntry.setId(userId);
        userEntry.setName(user.getName());
        userEntry.setEmail(user.getEmail());
        userEntry.setUsername(user.getUsername());

        String pwHash = PwHash.hashPassword(user.getPassword());
        userEntry.setHashpw(pwHash);

        String token = jwtLogic.generateToken(userId, user.getEmail());
        userEntry.setJwt(token);

        String curTime = Instant.now().toString();
        userEntry.setCreated(curTime);
        userEntry.setUpdated(curTime);

        UserTable savedUser = userRepository.save(userEntry); // save
        return new UserResponse(savedUser); // return custom
    }

    // generate token
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody Login user) {
        Optional<UserTable> userOpt = userRepository.findByUsername(user.getUsername());
        Map<String, Object> response = new HashMap<>();

        if (userOpt.isPresent()) {
            UserTable foundUser = userOpt.get();
            if (PwHash.verifyPassword(user.getPassword(), foundUser.getHashpw())) {
                String token = jwtLogic.generateToken(foundUser.getId(), foundUser.getEmail());

                foundUser.setJwt(token);
                userRepository.save(foundUser);

                response.put("jwt token", token);
                return ResponseEntity.ok(response); // 200
            }
        }

        response.put("error", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401
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

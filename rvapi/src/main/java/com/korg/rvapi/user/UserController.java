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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

        String pwHash = PwHash.createHashedPassword(user.getPassword());
        userEntry.setHashpw(pwHash);

        String token = jwtLogic.encodeJWT(userId, user.getEmail());
        userEntry.setJwt(token);

        String curTime = Instant.now().toString();
        userEntry.setCreated(curTime);
        userEntry.setUpdated(curTime);

        UserTable savedUser = userRepository.save(userEntry); // save
        return new UserResponse(savedUser); // return custom
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody Login user) {
        Optional<UserTable> userOpt = userRepository.findByUsername(user.getUsername());
        Map<String, Object> response = new HashMap<>();

        if (userOpt.isPresent()) {
            UserTable foundUser = userOpt.get();
            if (PwHash.compareToHashedPassword(user.getPassword(), foundUser.getHashpw())) {
                String token = jwtLogic.encodeJWT(foundUser.getId(), foundUser.getEmail());

                foundUser.setJwt(token);
                userRepository.save(foundUser);

                response.put("jwt token", token);
                return ResponseEntity.ok(response); // 200
            }
        }

        response.put("error", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401
    }

    @GetMapping
    public UserResponse getUser(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtLogic.decodeJWT(token);

        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(user);
        return response;
    }

    @PutMapping()
    public UserTable updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserCreate user) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtLogic.decodeJWT(token);
        UserTable existingUser = userRepository.findById(userId).get();

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setHashpw(user.getPassword());

        // create new jwt
        String newToken = jwtLogic.encodeJWT(existingUser.getId(), user.getEmail());
        existingUser.setJwt(newToken);

        return userRepository.save(existingUser);
    }

    @DeleteMapping()
    public String deleteUser(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtLogic.decodeJWT(token);

        try {
            userRepository.findById(userId).get();
            userRepository.deleteById(userId);
            return "User deleted successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }
}

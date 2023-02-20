package com.example.pizzaproject.user;

import com.example.pizzaproject.auth.JwtResponse;
import com.example.pizzaproject.auth.JwtUtil;
import com.example.pizzaproject.auth.RefreshRequest;
import com.example.pizzaproject.auth.RefreshUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Ref;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/get-all")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<JwtResponse> registerNewUser(@RequestBody User user) {
        try {
            userService.addNewUser(user);
            return UserService.createResponse(user);
        } catch (ResponseStatusException e) {
            JwtResponse response = new JwtResponse("Error while creating user", null, null);
            return new ResponseEntity<>(response, e.getStatusCode());
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        Optional<User> foundUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            return UserService.createResponse(foundUser.get());
        } else {
            return UserService.createErrorResponse();
        }
    }

    @PostMapping(path = "/admin-login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> adminlogin(@RequestBody User user) {
        Optional<User> foundUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            if (foundUser.get().isAdmin()) { // Check if user is an admin
                return UserService.createResponse(foundUser.get());
            } else {
                // User is not an admin
                return UserService.createErrorResponse();
            }
        } else {
            // User not found
            return UserService.createErrorResponse();
        }
    }

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        String email = RefreshUtil.getEmailFromRefreshToken(request.getRefreshToken());
        Optional<User> foundUser = userService.findUserByEmail(email);
        if (foundUser.isPresent()) {
            String jwtToken = JwtUtil.createJWT(foundUser.get());
            String refreshToken = RefreshUtil.createRefreshToken(foundUser.get());
            JwtResponse response = new JwtResponse("success", jwtToken, refreshToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            JwtResponse response = new JwtResponse("failure", null, null);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (JwtUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        String email = JwtUtil.getEmailFromJWTToken(token);
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/email")
    public String getEmailByToken(@RequestHeader("Authorization") String token) {
        String email = JwtUtil.getEmailFromJWTToken(token);
        return email;
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody(required = false) User user) {
        try {
            userService.updateUser(userId, user);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

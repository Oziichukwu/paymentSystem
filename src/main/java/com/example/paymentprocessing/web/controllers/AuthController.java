package com.example.paymentprocessing.web.controllers;


import com.example.paymentprocessing.data.dtos.RequestDtos.LoginRequest;
import com.example.paymentprocessing.data.dtos.RequestDtos.RegisterRequest;
import com.example.paymentprocessing.data.dtos.ResponseDtos.ApiResponse;
import com.example.paymentprocessing.data.dtos.ResponseDtos.JwtResponse;
import com.example.paymentprocessing.data.models.AppUser;
import com.example.paymentprocessing.data.models.Role;
import com.example.paymentprocessing.data.repository.RoleRepository;
import com.example.paymentprocessing.data.repository.UserRepository;
import com.example.paymentprocessing.security.JwtUtil;
import com.example.paymentprocessing.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        // Create new user's account
        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Assign roles
        Set<Role> roles = new HashSet<>();
        for (String roleName : registerRequest.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }
        user.setRoles(roles);

        // Save the user to the database
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.OK);
    }



    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtUtil.generateToken(loginRequest.getUsername(), auth.getAuthorities().iterator().next().getAuthority());
        return new JwtResponse(token);
    }
}

package com.credscope.credscope.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credscope.credscope.dto.LoginRequest;
import com.credscope.credscope.dto.LoginResponse;
import com.credscope.credscope.dto.RegisterRequest;
import com.credscope.credscope.entity.Role;
import com.credscope.credscope.entity.User;
import com.credscope.credscope.repository.UserRepository;
import com.credscope.credscope.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired 
	private UserRepository userRepository;
    @Autowired 
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
    	User user =new User();
    	user.setName(request.getName());
    	user.setEmail(request.getEmail());
    	user.setRole(Role.valueOf(request.getRole()));
    	user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    	userRepository.save(user);
    	return "User registered  successfully";
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setName(user.getName());
        response.setRole(user.getRole().name());
        return response;
    }

}

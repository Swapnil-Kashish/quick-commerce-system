package com.quickcommerce.user_service.service;

import com.quickcommerce.user_service.dto.LoginRequest;
import com.quickcommerce.user_service.dto.SignupRequest;
import com.quickcommerce.user_service.entity.User;
import com.quickcommerce.user_service.repository.UserRepository;
import com.quickcommerce.user_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public User signup(SignupRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        user.setRole("USER");
        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );
        if (!matches) {
            throw new RuntimeException(
                    "Invalid password"
            );
        }
        return jwtService.generateToken(
                user.getEmail()
        );
    }
}
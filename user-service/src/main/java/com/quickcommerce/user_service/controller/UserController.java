package com.quickcommerce.user_service.controller;

import com.quickcommerce.user_service.dto.LoginRequest;
import com.quickcommerce.user_service.dto.SignupRequest;
import com.quickcommerce.user_service.entity.User;
import com.quickcommerce.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public User signup(
            @RequestBody SignupRequest request
    ) {

        return userService.signup(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/profile")
    public String profile() {
        return "Protected Profile API";
    }
}
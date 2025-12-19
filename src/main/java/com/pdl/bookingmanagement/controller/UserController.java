package com.pdl.bookingmanagement.controller;

import com.pdl.bookingmanagement.dto.UserDTO;
import com.pdl.bookingmanagement.security.JwtUtil;
import com.pdl.bookingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String phone = request.get("phone");
        String password = request.get("password");

        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", 400,
                    "message", "Vui lòng nhập email hoặc số điện thoại!"
            ));
        }

        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", 400,
                    "message", "Vui lòng nhập mật khẩu!"
            ));
        }


        Map<String, Object> userResult = userService.login(email, phone, password);

        if(!userResult.get("status").equals(200)){
            return ResponseEntity.badRequest().body(Map.of(
                    "status", userResult.get("status"),
                    "message",  userResult.get("message")
            ));
        }

        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", userResult.get("message"),
                "token", token,
                "data", userResult.get("data")
        ));
    }
}

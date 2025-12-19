package com.pdl.bookingmanagement.service.Impl;

import com.pdl.bookingmanagement.dto.UserDTO;
import com.pdl.bookingmanagement.model.User;
import com.pdl.bookingmanagement.repository.UserRepository;
import com.pdl.bookingmanagement.security.JwtUtil;
import com.pdl.bookingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Map<String, Object> login(String phone, String email, String password) {
        User u = userRepository.findByPhoneOrEmail(phone, email);
        if(u == null){
            return Map.of(
                    "status", 404,
                    "message", "Email hoặc số điện thoại này chưa được đăng ký!"
            );
        }

        if(!passwordEncoder.matches(password, u.getPassword())){
            return Map.of(
              "status", 400,
              "message", "Mật khẩu không chính xác!"
            );
        }

        if(!u.isStatus()){
            return Map.of(
                    "status", 400,
                    "message", "Tài khoản này đang bị khóa!"
            );
        }

        Map<String, Object> userData = Map.of(
                "user", u
        );

        return Map.of(
                "status", 200,
                "data", userData,
                "message", "Đăng nhập thành công!"
        );
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        dto.setPassword(user.getPassword());
        dto.setFullname(user.getFullName());
        dto.setRole(user.getRole());
        dto.setStatus(user.isStatus());
        dto.setNote(user.getNote());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

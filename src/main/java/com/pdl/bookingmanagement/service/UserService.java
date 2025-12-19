package com.pdl.bookingmanagement.service;

import java.util.Map;

public interface UserService {
    Map<String, Object> login (String phone, String email, String password);
}

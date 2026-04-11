package com.wu.service;

import com.wu.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password);
    User findByUsername(String username);
    void createAdminUser();
}
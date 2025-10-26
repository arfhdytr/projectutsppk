package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.UserDto;
import com.polstat.sipenat.auth.dto.RegisterRequest;

public interface UserService {
    UserDto register(RegisterRequest request);
    UserDto getUserByEmail(String email);
    UserDto updateProfile(String email, UserDto userDto);
    void changePassword(String email, String oldPassword, String newPassword);
    void deleteAccount(String email);
}
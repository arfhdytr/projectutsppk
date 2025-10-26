package com.polstat.sipenat.auth.dto;

import com.polstat.sipenat.entity.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String username;
    private String fullName;
    private Role role;
}
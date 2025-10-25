package com.polstat.sipenat.auth.dto;

// auth/dto/RegisterRequest.java
public record RegisterRequest(String username, String email, String password, String fullName, String nimNip, String phoneNumber, String role) {}

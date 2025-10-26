package com.polstat.sipenat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String fullName;
    private String nimNip;
    private String phoneNumber;
    private String address;

//    // Manual getters
//    public String getFullName() { return fullName; }
//    public String getNimNip() { return nimNip; }
//    public String getPhoneNumber() { return phoneNumber; }
//    public String getAddress() { return address; }
//
}
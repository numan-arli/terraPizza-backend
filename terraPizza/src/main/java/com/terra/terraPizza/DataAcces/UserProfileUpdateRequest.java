package com.terra.terraPizza.DataAcces;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthDate;
    private String gender;
    private boolean sms;
    private boolean emailPermission;
    private boolean phonePermission;
}

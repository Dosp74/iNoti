package com.example.Notice_reminder.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

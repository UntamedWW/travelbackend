package com.travel.travelbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 5)
    private String password;
}

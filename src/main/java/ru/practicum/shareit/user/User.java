package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class User {
    private int id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}

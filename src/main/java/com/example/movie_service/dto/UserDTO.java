package com.example.movie_service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String displayName;
    private String username;
    private String password;
    private String role;
}

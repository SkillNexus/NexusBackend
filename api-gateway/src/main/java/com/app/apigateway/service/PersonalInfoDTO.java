package com.app.apigateway.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDTO {
    private String username;
    private String bio;
    private String profilePictureUrl;
} 
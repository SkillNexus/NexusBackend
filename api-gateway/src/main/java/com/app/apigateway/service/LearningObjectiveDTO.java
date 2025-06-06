package com.app.apigateway.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningObjectiveDTO {
    private String id;
    private String title;
    private String description;
    private int progressPercentage;
} 
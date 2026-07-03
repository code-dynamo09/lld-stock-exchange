package com.algo.stock_exchange.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Builder.Default
    private String userId = UUID.randomUUID().toString();

    @NotBlank(message = "User name is required")
    private String name;

    @Email(message = "Invalid email address")
    private String emailId;

}

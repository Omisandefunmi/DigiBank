package com.semicolon.DigiBank.data.models;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Account account;
    private Set<Role> roles;
}

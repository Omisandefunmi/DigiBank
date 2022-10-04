package com.semicolon.DigiBank.data.models;

import com.semicolon.DigiBank.data.models.enums.RoleType;
import lombok.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Account account;
    private Set<RoleType> roles;


}

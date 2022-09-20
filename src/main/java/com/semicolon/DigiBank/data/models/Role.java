package com.semicolon.DigiBank.data.models;

import com.semicolon.DigiBank.data.models.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Role {
    private String id;
    private RoleType roleType;

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }
}

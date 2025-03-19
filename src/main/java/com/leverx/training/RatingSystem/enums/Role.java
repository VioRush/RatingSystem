package com.leverx.training.RatingSystem.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMINISTRATOR,
    SELLER,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}

package com.leverx.training.RatingSystem.db.dto;

import lombok.*;

import java.time.Instant;

@Data
public class AnonymousUserDTO extends BaseUserDTO {

    private final String NAME = "Anonymous user";

    public AnonymousUserDTO(Instant createdAt) {
        super(createdAt);
    }
}

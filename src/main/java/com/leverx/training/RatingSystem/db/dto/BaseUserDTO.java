package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUserDTO extends RepresentationModel<BaseUserDTO> {

    private Instant createdAt;
}

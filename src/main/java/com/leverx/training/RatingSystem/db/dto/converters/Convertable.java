package com.leverx.training.RatingSystem.db.dto.converters;

public interface Convertable<D, E> {
    D convertToDTO(E entity);
    E convertToEntity(D dto);
}

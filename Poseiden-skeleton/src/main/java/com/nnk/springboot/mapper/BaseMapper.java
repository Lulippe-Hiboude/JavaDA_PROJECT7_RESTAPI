package com.nnk.springboot.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;


public interface BaseMapper<E, C, R, U> {

    E toEntity(final C createDto);

    E toEntity(final U updateDto, @MappingTarget final E entity);

    R toEntityDto(final E entity);

    U toUpdateDto(final E entity);

    default List<R> toDtoList(final List<E> entities) {
        return entities.stream()
                .map(this::toEntityDto)
                .toList();
    }
}

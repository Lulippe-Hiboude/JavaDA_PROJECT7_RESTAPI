package com.nnk.springboot.service;

import java.util.List;

public interface EntityService<C, R, U> {
    void handleEntityCreation(C entityCreateDto);

    void handleEntityDeletion(final Integer id);

    List<R> findAllEntity();

    void handleEntityUpdate(final U entityUpdateDto);

    U getEntityUpdateDto(final Integer id);
}

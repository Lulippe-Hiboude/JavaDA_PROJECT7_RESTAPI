package com.nnk.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEntityService<E, C, R, U> implements EntityService<C, R, U> {
    protected final JpaRepository<E, Integer> entityRepository;

    @Override
    public List<R> findAllEntity() {
        return toDtoList(entityRepository.findAll());
    }

    @Override
    public void handleEntityCreation(final C entityCreateDto) {
        log.info("Handle user creation");
        processEntityCreation(entityCreateDto);
    }

    @Override
    public void handleEntityDeletion(final Integer id) {
        entityRepository.findById(id)
                .ifPresentOrElse(
                        existingEntity -> entityRepository.deleteById(id),
                        () -> handleError(id)
                );
    }

    @Override
    public U getEntityUpdateDto(final Integer id) {
        final E entity = entityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        return getEntityUpdateDto(entity);
    }

    @Override
    public void handleEntityUpdate(final U entityUpdateDto) {
        checkEntityValidity(entityUpdateDto);
        entityRepository.findById(getEntityId(entityUpdateDto))
                .ifPresent(entityToUpdate -> entityRepository.save(
                        getUpdatedEntity(entityUpdateDto, entityToUpdate))
                );
    }

    protected void createEntity(final C entityCreateDto) {
        entityRepository.save(getEntity(entityCreateDto));
    }

    protected abstract void checkEntityValidity(final U entityUpdateDto);

    protected abstract void processEntityCreation(final C entityCreateDto);

    protected abstract Integer getEntityId(final U entityUpdateDto);

    protected abstract E getUpdatedEntity(final U entityUpdateDto, final E entity);

    protected abstract U getEntityUpdateDto(final E entityUpdateDto);

    protected abstract E getEntity(final C entityCreateDto);

    protected abstract void handleError(final Integer id);

    protected abstract List<R> toDtoList(List<E> entities);
}

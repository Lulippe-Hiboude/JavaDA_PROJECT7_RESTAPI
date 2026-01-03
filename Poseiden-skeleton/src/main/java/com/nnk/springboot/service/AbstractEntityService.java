package com.nnk.springboot.service;

import com.nnk.springboot.mapper.BaseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class AbstractEntityService<E, C, R, U>
        implements EntityService<C, R, U> {
    protected final JpaRepository<E, Integer> entityRepository;
    protected final BaseMapper<E, C, R, U> mapper;

    @Override
    public List<R> findAllEntity() {
        return mapper.toDtoList(entityRepository.findAll());
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
        return mapper.toUpdateDto(entity);
    }

    @Override
    public void handleEntityUpdate(final U entityUpdateDto) {
        checkEntityValidity(entityUpdateDto);
        entityRepository.findById(getEntityId(entityUpdateDto))
                .ifPresent(entityToUpdate -> entityRepository.save(getUpdatedEntity(entityUpdateDto, entityToUpdate))
                );
    }

    protected void createEntity(final C entityCreateDto) {
        entityRepository.save(mapper.toEntity(entityCreateDto));
    }

    protected E getUpdatedEntity(final U entityUpdateDto, final E entity) {
        return mapper.toEntity(entityUpdateDto, entity);
    }

    protected void processEntityCreation(final C entityCreateDto) {
        log.info("Process creation for entity: {}", entityCreateDto);
        createEntity(entityCreateDto);
    }

    protected abstract void checkEntityValidity(final U entityUpdateDto);

    protected abstract Integer getEntityId(final U entityUpdateDto);

    protected abstract void handleError(final Integer id);
}

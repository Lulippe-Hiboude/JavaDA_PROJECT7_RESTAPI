package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.rule_name.RuleNameCreateDto;
import com.nnk.springboot.dto.rule_name.RuleNameDto;
import com.nnk.springboot.dto.rule_name.RuleNameUpdateDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.RuleNameService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class RuleNameServiceImpl extends AbstractEntityService<RuleName, RuleNameCreateDto, RuleNameDto, RuleNameUpdateDto>
        implements RuleNameService {

    public RuleNameServiceImpl(final RuleNameRepository repository, final RuleNameMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected void checkEntityValidity(final RuleNameUpdateDto entityUpdateDto) {
    }

    @Override
    protected Integer getEntityId(final RuleNameUpdateDto ruleNameUpdateDto) {
        return ruleNameUpdateDto.getId();
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("The rule name with id " + id + " was not found");
    }
}

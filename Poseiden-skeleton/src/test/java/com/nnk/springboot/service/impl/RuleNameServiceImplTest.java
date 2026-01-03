package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.rule_name.RuleNameCreateDto;
import com.nnk.springboot.dto.rule_name.RuleNameDto;
import com.nnk.springboot.dto.rule_name.RuleNameUpdateDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceImplTest {

    private RuleNameServiceImpl ruleNameServiceImpl;

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Captor
    private ArgumentCaptor<RuleName> ruleNameArgumentCaptor;

    @BeforeEach
    void setUp() {
        final RuleNameMapper ruleNameMapper = Mappers.getMapper(RuleNameMapper.class);
        ruleNameServiceImpl = new RuleNameServiceImpl(ruleNameRepository, ruleNameMapper);
    }

    @Test
    @DisplayName("Should create a new ruleName")
    void shouldCreateNewRuleName() {
        //given
        final String name = "name";
        final String description = "description";
        final String json = "Json";
        final String sqlPart = "SqlPart";
        final String sqlStr = "SqlString";
        final String template = "Template";
        final RuleNameCreateDto ruleNameCreateDto = RuleNameCreateDto.builder()
                .name(name)
                .json(json)
                .description(description)
                .sqlPart(sqlPart)
                .sqlStr(sqlStr)
                .template(template)
                .build();

        //when
        ruleNameServiceImpl.handleEntityCreation(ruleNameCreateDto);

        //then
        verify(ruleNameRepository, times(1)).save(ruleNameArgumentCaptor.capture());
        RuleName ruleName = ruleNameArgumentCaptor.getValue();
        assertNotNull(ruleName);
        assertEquals(name, ruleName.getName());
        assertEquals(description, ruleName.getDescription());
        assertEquals(sqlPart, ruleName.getSqlPart());
        assertEquals(sqlStr, ruleName.getSqlStr());
        assertEquals(template, ruleName.getTemplate());
        assertEquals(json, ruleName.getJson());

    }

    @Test
    @DisplayName("should update existing RuleName")
    void shouldUpdateExistingRuleName() {
        //given
        final int id = 5;
        final String updatedName = "updated name";
        final String updatedDescription = "updated description";
        final String updatedJson = "updated Json";
        final String updatedSqlPart = "updated SqlPart";
        final String updatedSqlStr = "updated SqlString";
        final String updatedTemplate = "updated Template";

        final RuleName existingRule = getRule(id);

        final RuleNameUpdateDto ruleNameUpdateDto = RuleNameUpdateDto.builder()
                .id(id)
                .name(updatedName)
                .json(updatedJson)
                .description(updatedDescription)
                .sqlPart(updatedSqlPart)
                .sqlStr(updatedSqlStr)
                .template(updatedTemplate)
                .build();

        given(ruleNameRepository.findById(id)).willReturn(Optional.of(existingRule));

        //when
        ruleNameServiceImpl.handleEntityUpdate(ruleNameUpdateDto);

        //then
        verify(ruleNameRepository, times(1)).save(ruleNameArgumentCaptor.capture());
        RuleName updatedRule = ruleNameArgumentCaptor.getValue();
        assertEquals(updatedName, updatedRule.getName());
        assertEquals(updatedDescription, updatedRule.getDescription());
        assertEquals(updatedJson, updatedRule.getJson());
        assertEquals(updatedSqlPart, updatedRule.getSqlPart());
        assertEquals(updatedSqlStr, updatedRule.getSqlStr());
        assertEquals(updatedTemplate, updatedRule.getTemplate());
        assertEquals(id, updatedRule.getId());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final int id = 1;
        given(ruleNameRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> ruleNameServiceImpl.handleEntityDeletion(id));
        verify(ruleNameRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("should delete existing RuleName by id")
    void shouldDeleteExistingRuleNameById() {
        //given
        final int id = 5;
        final RuleName existingRule = getRule(id);
        given(ruleNameRepository.findById(id)).willReturn(Optional.of(existingRule));

        //when
        ruleNameServiceImpl.handleEntityDeletion(id);

        //then
        verify(ruleNameRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("should return a list of RuleNameDto")
    void shouldReturnAListOfRuleNameDto() {
        //given
        final int id = 5;
        final RuleName rule = getRule(id);
        final List<RuleName> ruleList = List.of(rule);
        given(ruleNameRepository.findAll()).willReturn(ruleList);

        //when
        final List<RuleNameDto> list = ruleNameServiceImpl.findAllEntity();

        //then
        assertNotNull(list);
        assertThat(list)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "description", "json", "id", "template", "sqlStr", "sqlPart")
                .isEqualTo(ruleList.get(0));
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        given(ruleNameRepository.findAll()).willReturn(Collections.emptyList());

        //when
        final List<RuleNameDto> list = ruleNameServiceImpl.findAllEntity();

        //then
        assertNotNull(list);
        assertThat(list).isEmpty();
    }

    @Test
    @DisplayName("should return RuleNameUpdateDto")
    void shouldReturnRuleNameUpdateDto() {
        //given
        final int id = 5;
        final RuleName existingRule = getRule(id);
        given(ruleNameRepository.findById(id)).willReturn(Optional.of(existingRule));

        //when
        final RuleNameUpdateDto ruleNameUpdateDto = ruleNameServiceImpl.getEntityUpdateDto(id);

        //then
        assertNotNull(ruleNameUpdateDto);
        assertEquals(id, ruleNameUpdateDto.getId());
        assertEquals(existingRule.getName(), ruleNameUpdateDto.getName());
        assertEquals(existingRule.getDescription(), ruleNameUpdateDto.getDescription());
        assertEquals(existingRule.getSqlPart(), ruleNameUpdateDto.getSqlPart());
        assertEquals(existingRule.getSqlStr(), ruleNameUpdateDto.getSqlStr());
        assertEquals(existingRule.getTemplate(), ruleNameUpdateDto.getTemplate());
        assertEquals(existingRule.getJson(), ruleNameUpdateDto.getJson());
    }

    private static RuleName getRule(final int id) {

        final String name = "name";
        final String description = "description";
        final String json = "Json";
        final String sqlPart = "SqlPart";
        final String sqlStr = "SqlString";
        final String template = "Template";
        return RuleName.builder()
                .id(id)
                .name(name)
                .json(json)
                .description(description)
                .sqlPart(sqlPart)
                .sqlStr(sqlStr)
                .template(template)
                .build();
    }
}
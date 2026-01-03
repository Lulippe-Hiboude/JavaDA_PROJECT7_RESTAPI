package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.rule_name.RuleNameCreateDto;
import com.nnk.springboot.dto.rule_name.RuleNameDto;
import com.nnk.springboot.dto.rule_name.RuleNameUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RuleNameMapper extends BaseMapper<RuleName, RuleNameCreateDto, RuleNameDto, RuleNameUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "json", source = "json")
    @Mapping(target = "template", source = "template")
    @Mapping(target = "sqlStr", source = "sqlStr")
    @Mapping(target = "sqlPart", source = "sqlPart")
    RuleName toEntity(final RuleNameCreateDto ruleNameCreateDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "json", source = "json")
    @Mapping(target = "template", source = "template")
    @Mapping(target = "sqlStr", source = "sqlStr")
    @Mapping(target = "sqlPart", source = "sqlPart")
    RuleName toEntity(final RuleNameUpdateDto ruleNameUpdateDto, @MappingTarget final RuleName ruleName);
}

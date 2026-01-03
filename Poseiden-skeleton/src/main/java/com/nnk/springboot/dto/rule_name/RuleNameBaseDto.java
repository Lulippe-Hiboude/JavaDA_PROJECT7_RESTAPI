package com.nnk.springboot.dto.rule_name;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RuleNameBaseDto {

    @NotBlank(message = "The name of the rule is required")
    @Length(max = 125, message = "the name cannot exceed 125 characters")
    private String name;

    @NotBlank(message = "The description of the rule is required")
    @Length(max = 125, message = "the description cannot exceed 125 characters")
    private String description;

    @Length(max = 125, message = "the json cannot exceed 125 characters")
    private String json;

    @Length(max = 512, message = "the template cannot exceed 512 characters")
    private String template;

    @Length(max = 125, message = "the sqlStr cannot exceed 125 characters")
    private String sqlStr;

    @Length(max = 125, message = "the sqlPart cannot exceed 125 characters")
    private String sqlPart;
}

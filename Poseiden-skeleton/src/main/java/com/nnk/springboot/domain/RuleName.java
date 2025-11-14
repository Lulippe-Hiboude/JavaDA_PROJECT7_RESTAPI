package com.nnk.springboot.domain;

import jakarta.persistence.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleName {
    // TODO: Map columns in data table RULENAME with corresponding java fields
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name ="Id")
    private Integer id;

    @Column(name = "name", length = 125)
    @NotBlank(message = "Name is mandatory")
    @Length(max = 125, message = "Name cannot be longer than 125 characters" )
    private String name;

    @Column(name = "description", length = 125)
    private String description;

    @Column(name = "json", length = 125)
    private String json;

    @Column(name = "template", length = 512)
    private String template;

    @Column(name = "sqlStr", length = 125)
    private String sqlStr;

    @Column(name = "sqlPart", length = 125)
    private String sqlPart;
}

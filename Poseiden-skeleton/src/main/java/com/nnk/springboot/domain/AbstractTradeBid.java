package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractTradeBid {
    @Column(name = "account")
    @NotBlank(message = "Account is mandatory")
    @Length(max = 30, message = "Account cannot be longer than 30 characters")
    private String account;

    @Column(name = "type")
    @NotBlank(message = "Type is mandatory")
    @Length(max = 30, message = "Type cannot be longer than 30 characters")
    private String type;

    @Column(name = "benchmark")
    @Length(max = 125, message = "Benchmark cannot be longer than 125 characters")
    private String benchmark;

    @Column(name = "security")
    private String security;

    @Column(name = "status")
    @Length(max = 10, message = "Status cannot be longer than 10 characters")
    private String status;

    @Column(name = "trader")
    private String trader;

    @Column(name = "book")
    @Length(max = 125, message = "book cannot be longer than 125 characters")
    private String book;

    @Column(name = "creation_name")
    private String creationName;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "revisionName")
    private String revisionName;

    @Column(name = "revision_date")
    private LocalDateTime revisionDate;

    @Column(name = "deal_name")
    @Length(max = 125, message = "Deal Name cannot be longer than 125 characters")
    private String dealName;

    @Column(name = "deal_type")
    @Length(max = 125, message = "Deal Type cannot be longer than 125 characters")
    private String dealType;

    @Column(name = "source_list_id")
    private String sourceListId;

    @Column(name = "side")
    private String side;
}

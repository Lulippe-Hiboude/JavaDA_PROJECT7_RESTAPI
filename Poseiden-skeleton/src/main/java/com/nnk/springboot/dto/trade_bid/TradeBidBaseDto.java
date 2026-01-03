package com.nnk.springboot.dto.trade_bid;

import jakarta.persistence.Column;
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
public class TradeBidBaseDto {

    @Column(name = "account")
    @NotBlank(message = "Account is mandatory")
    @Length(max = 30, message = "Account cannot be longer than 30 characters")
    private String account;

    @Column(name = "type")
    @NotBlank(message = "Type is mandatory")
    @Length(max = 30, message = "Type cannot be longer than 30 characters")
    private String type;
}

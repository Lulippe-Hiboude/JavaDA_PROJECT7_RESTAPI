package com.nnk.springboot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ViewEnum {
    ADD("/add"),
    LIST("/list"),
    UPDATE("/update");

    final String value;
}

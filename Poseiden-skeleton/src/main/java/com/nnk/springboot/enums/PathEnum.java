package com.nnk.springboot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PathEnum {
    PATH_ADMIN_HOME("/admin/home"),
    PATH_USER_HOME("/home"),
    PATH_ERROR("/auth/login?error");

    final String value;
}

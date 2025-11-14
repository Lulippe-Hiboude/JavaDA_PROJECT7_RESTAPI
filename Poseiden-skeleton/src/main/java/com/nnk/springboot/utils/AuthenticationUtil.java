package com.nnk.springboot.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@UtilityClass
@Slf4j
public class AuthenticationUtil {

    public static String getAuthenticatedUsername(){
       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
       log.debug(String.valueOf(authentication));
        if(authentication==null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        return authentication.getName();
    }
}

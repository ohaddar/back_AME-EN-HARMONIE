package com.doranco.project.utils;

import com.doranco.project.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserRoleExtractor {
    static  public boolean isUser(Authentication authentication) {
        boolean hasUserRole =false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority:authorities) {
            if(authority.getAuthority().equals(RoleEnum.USER.toString())) {
                hasUserRole=true;
            }
        }
        return  hasUserRole;
    }
    static public boolean isAdmin(Authentication authentication) {
        boolean hasAdminRole = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority:authorities) {
            if(authority.getAuthority().equals(RoleEnum.USER.toString())) {
                hasAdminRole= true;
            }
        }
        return hasAdminRole;
    }
}

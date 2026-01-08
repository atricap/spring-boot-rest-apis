package com.luv2code.springboot.todos.entity;

import jakarta.persistence.Embeddable;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
public class Authority implements GrantedAuthority {

    public static final String STR_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String STR_ADMIN = "ROLE_ADMIN";
    public static final Authority EMPLOYEE = new Authority(STR_EMPLOYEE);
    public static final Authority ADMIN = new Authority(STR_ADMIN);

    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public static boolean isEmployee(GrantedAuthority auth) {
        return STR_EMPLOYEE.equals(auth.getAuthority());
    }

    public static boolean isAdmin(GrantedAuthority auth) {
        return STR_ADMIN.equals(auth.getAuthority());
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

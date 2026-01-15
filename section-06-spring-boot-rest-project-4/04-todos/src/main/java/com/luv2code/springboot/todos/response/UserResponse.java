package com.luv2code.springboot.todos.response;

import com.luv2code.springboot.todos.entity.Authority;
import com.luv2code.springboot.todos.entity.User;

import java.util.List;
import java.util.Objects;

public class UserResponse {

    private long id;

    private String fullName;

    private String email;

    private List<Authority> authorities;

    public UserResponse(long id, String fullName, String email, List<Authority> authorities) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                "%s %s".formatted(user.getFirstName(), user.getLastName()),
                user.getEmail(),
                user.getAuthorities().stream()
                        .map(auth -> (Authority) auth)
                        .toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserResponse that))
            return false;
        return id == that.id
                && Objects.equals(fullName, that.fullName)
                && Objects.equals(email, that.email)
                && Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, authorities);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}

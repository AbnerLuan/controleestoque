package com.luan.controleestoque.dto;

import com.luan.controleestoque.model.Enum.Profile;
import com.luan.controleestoque.model.User;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;

    @NotNull(message = "Field Required")
    protected String name;

    @NotNull
    protected String email;

    @NotNull
    protected String password;

    protected Set<Integer> profile = new HashSet<>();

    public UserDTO() {
        super();
        addProfile(Profile.ADMIN);
    }

    public UserDTO(User obj) {
        super();
        this.id = obj.getId();
        this.name = obj.getName();
        this.email = obj.getEmail();
        this.password = obj.getPassword();
        this.profile = obj.getProfile().stream().map(x -> x.getCode()).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Integer> getProfile() {
        return profile;
    }

    public void addProfile(Profile profile) {
        this.profile.add(profile.getCode());
    }


}

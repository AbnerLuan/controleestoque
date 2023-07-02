package com.luan.controleestoque.model;

import com.luan.controleestoque.dto.UserDTO;
import com.luan.controleestoque.model.Enum.Profile;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends Person {
    private static final long serialVersionUID = 1L;

    public User() {
        super();
        addProfile(Profile.ADMIN);
    }


    public User(Long id, String name, String email, String password) {
        super(id, name, email, password);
        addProfile(Profile.USER);
    }

    public User(UserDTO obj) {
        super();
        this.id = obj.getId();
        this.name = obj.getName();
        this.email = obj.getEmail();
        this.password = obj.getPassword();
        this.profile = obj.getProfile();
    }

}

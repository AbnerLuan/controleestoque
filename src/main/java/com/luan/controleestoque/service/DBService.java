package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Enum.Profile;
import com.luan.controleestoque.model.User;
import com.luan.controleestoque.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
    @Service
    public class DBService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder encoder;

        public void instanciaDB() {

            User admin = new User(null, "admin", "gauxossaogays@gmail.com",  encoder.encode("gauxosaogays"));
            admin.addProfile(Profile.ADMIN);

            userRepository.save(admin);
        }
    }


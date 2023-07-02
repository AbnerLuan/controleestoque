package com.luan.controleestoque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ControleestoqueApplication implements ApplicationRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ControleestoqueApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        User user = createUser(); // Crie o usuário que deseja salvar

//        userRepository.save(user); // Salve o usuário no banco de dados
    }

//    private User createUser() {
//        User user = new User();
//        // Preencha os dados do usuário
//        user.setName("Abner");
//        user.setCpf("9999999");
//        user.setEmail("abner2@email.com");
//        String password = "1234";
//        String encryptedPassword = passwordEncoder.encode(password);
//        user.setPassword(encryptedPassword);
//
//        return user;
//    }
}





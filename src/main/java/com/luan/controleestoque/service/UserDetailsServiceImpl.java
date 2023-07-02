package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Person;
import com.luan.controleestoque.repository.PersonRepository;
import com.luan.controleestoque.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return new UserSS(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getPassword(),
                    user.get().getProfile());
        }

        throw new UsernameNotFoundException(email);
    }
}

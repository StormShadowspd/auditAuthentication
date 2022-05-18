package com.audit.authentication.service;

import com.audit.authentication.model.UserRecord;
import com.audit.authentication.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    AuthenticationRepository repository;

    public void saveUser(UserRecord userRecord) {
        repository.save(userRecord);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRecord userRecord = repository.findByUserName(username);
        System.out.println(userRecord);
        if (userRecord==null) {
            return null;
        }
        return new User(userRecord.getUserName(), userRecord.getUserPassword(), new ArrayList<>());
    }
}

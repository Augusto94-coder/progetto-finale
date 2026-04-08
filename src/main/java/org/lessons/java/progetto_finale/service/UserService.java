package org.lessons.java.progetto_finale.service;


import org.lessons.java.progetto_finale.model.User;
import org.lessons.java.progetto_finale.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    
}
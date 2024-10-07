package com.invima.scaffolding.application.impl;

import com.invima.scaffolding.application.UserService;
import com.invima.scaffolding.domain.model.User;
import com.invima.scaffolding.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(User user) {
        userRepository.saveUser(user);
    }
}

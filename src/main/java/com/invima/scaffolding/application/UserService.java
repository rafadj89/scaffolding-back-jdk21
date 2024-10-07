package com.invima.scaffolding.application;

import com.invima.scaffolding.domain.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    void createUser(User user);
}

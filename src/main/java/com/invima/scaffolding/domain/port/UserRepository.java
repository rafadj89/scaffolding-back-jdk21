package com.invima.scaffolding.domain.port;

import com.invima.scaffolding.domain.model.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);
    void saveUser(User user);
}

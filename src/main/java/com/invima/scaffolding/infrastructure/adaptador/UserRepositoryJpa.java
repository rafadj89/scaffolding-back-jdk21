package com.invima.scaffolding.infrastructure.adaptador;

import com.invima.scaffolding.domain.model.User;
import com.invima.scaffolding.domain.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findById(Long id);

    default void saveUser(User user) {
        save(user);
    }

}

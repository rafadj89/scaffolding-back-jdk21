package com.invima.scaffolding.application;

import com.invima.scaffolding.application.dto.Encrypte;
import com.invima.scaffolding.domain.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Encrypte> getUserById(Long id) throws Exception;

    void createUser(User user);
}

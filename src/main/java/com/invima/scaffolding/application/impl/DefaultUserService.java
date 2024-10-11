package com.invima.scaffolding.application.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invima.scaffolding.application.UserService;
import com.invima.scaffolding.application.dto.Encrypte;
import com.invima.scaffolding.application.util.EncryptionUtil;
import com.invima.scaffolding.domain.model.User;
import com.invima.scaffolding.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final String PREDEFINED_AES_KEY = "bWJZdGhYbnZSejVxekp6WWs3dk90aXds";  // 16 bytes
    public ResponseEntity<Encrypte> getUserById(Long id) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<User> user = userRepository.findById(id);

        String jsonString = objectMapper.writeValueAsString(user.get());
        String encryptedResponse = EncryptionUtil.encrypt(jsonString, PREDEFINED_AES_KEY);
        Encrypte encrypte = new Encrypte();
        encrypte.setHash(encryptedResponse);
        return ResponseEntity.ok(encrypte);
    }

    public void createUser(User user) {
        userRepository.saveUser(user);
    }
}

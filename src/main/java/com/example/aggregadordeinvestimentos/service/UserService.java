package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.CreateUserDto;
import com.example.aggregadordeinvestimentos.controller.UpdateUserDto;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {

        // DTO -> ENTITY
        var entity = new User(
                UUID.randomUUID(),
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public Optional<User> deleteUser(String userId) {
        var user = userRepository.findById(UUID.fromString(userId));
        if (user.isPresent()) {
            try {
                userRepository.delete(user.get());
            } catch (OptimisticLockException e) {
                return Optional.empty();
            }
        }
        return user;
    }

    public Optional<User> updateUser(String userId, UpdateUserDto updateUserDto) {
        var userEntity =  userRepository.findById(UUID.fromString(userId));
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }
            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            try {
                return Optional.of(userRepository.save(user));
            } catch (OptimisticLockException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}

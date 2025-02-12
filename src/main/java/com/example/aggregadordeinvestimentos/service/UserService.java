package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateUserDto;
import com.example.aggregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.example.aggregadordeinvestimentos.entity.Account;
import com.example.aggregadordeinvestimentos.entity.BillingAddress;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.BillingAddressRepository;
import com.example.aggregadordeinvestimentos.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var user = userRepository.existsById(id);

        if (user) {
            userRepository.deleteById(id);
        }
    }

    public void updateUser(String userId, UpdateUserDto updateUserDto) {
        var userEntity =  userRepository.findById(UUID.fromString(userId));
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }
            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user =  userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> ENTITY
        var account = new Account(
                UUID.randomUUID(),
                user,
                null,
                new ArrayList<>(),
                createAccountDto.description()
        );

        // save account
        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccount_id(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );

        // save billing address
        billingAddressRepository.save(billingAddress);
    }
}

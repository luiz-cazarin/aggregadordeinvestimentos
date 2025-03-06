package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.dto.AccountResponseDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateUserDto;
import com.example.aggregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.example.aggregadordeinvestimentos.entity.Account;
import com.example.aggregadordeinvestimentos.entity.BillingAddress;
import com.example.aggregadordeinvestimentos.entity.Role;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.BillingAddressRepository;
import com.example.aggregadordeinvestimentos.repository.RoleRepository;
import com.example.aggregadordeinvestimentos.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        // DTO -> ENTITY
        var entity = new User(
                UUID.randomUUID(),
                createUserDto.username(),
                createUserDto.email(),
                passwordEncoder.encode(createUserDto.password()),
                Instant.now(),
                null,
                basicRole);

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

    public List<AccountResponseDto> getAccounts(String userId) {
        var user =  userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts().stream()
                .map(ac -> new AccountResponseDto(
                        ac.getAccount_id().toString(),
                        ac.getDescription())
                ).toList();
    }
}

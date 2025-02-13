package com.example.aggregadordeinvestimentos.controller;

import com.example.aggregadordeinvestimentos.controller.dto.AccountResponseDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateUserDto;
import com.example.aggregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.example.aggregadordeinvestimentos.entity.Account;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserList() {
        var listUser = userService.getUserList();
        if (!listUser.isEmpty()) {
            return ResponseEntity.ok(listUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUsserById(@PathVariable("userId") String userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<User> createAccount(@PathVariable("userId") String userId, @RequestBody CreateAccountDto createAccountDto) {
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAccounts(@PathVariable("userId") String userId) {
        var accounts = userService.getAccounts(userId);
        return ResponseEntity.ok(accounts);
    }
}

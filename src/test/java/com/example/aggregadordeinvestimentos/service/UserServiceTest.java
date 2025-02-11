package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.CreateUserDto;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

// MockitoExtension é uma extensão do JUnit 5 que permite a utilização de mocks
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /* Todo teste tem que passar por 3 passos
     Arrange - Configuração
     Act - Ação
     Assert - Verificação */

    // Criando um mock do UserRepository
    @Mock
    private UserRepository userRepository;

    // Injetando o mock do UserRepository no UserService
    @InjectMocks
    private UserService userService;

    // Criando um captor para capturar o usuário que está sendo salvo
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    // Criando uma classe interna para agrupar os testes de createUser
    @Nested
    class createUser {

        // Teste para verificar se o método createUser está criando um usuário
        // Por padrão e bom usar o "should" no nome do teste
        @Test
        @DisplayName("Should create user with success")
        void shouldCreateUserWithSeccess() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null
            );
            // doReturn(user).when(userRepository).save(any()); // any nao e uma boa pratica para cenaarios de sucesso
            doReturn(user).when(userRepository).save(userCaptor.capture()); // passar a estrutura para validar o teste
            var input = new CreateUserDto(
                    "username",
                    "user@mail.com",
                    "1234");
            // Act
            var output = userService.createUser(input);
            // Assert

            var userCaptured = userCaptor.getValue();

            // assertNotNull(output); // no caso de any

            // garantir que o username do usuario capturado é igual ao username do usuario criado
            assertEquals(user.getUsername(), userCaptured.getUsername());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(user.getPassword(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should throw exception when error occours")
        void shouldThrowExceptionWhenErrorOccours() {
            // Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "user@mail.com",
                    "1234");
            // Act & assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }

    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should return user when user exists when optional is present")
        void shouldReturnUserWhenUserExists() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidCaptor.capture());
            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidCaptor.getValue());
        }

        @Test
        @DisplayName("Should return user when user exists when optional is empty")
        void shouldReturnUserWhenUserExistsWhenOptionalIsEmpty() {
            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidCaptor.capture());
            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidCaptor.getValue());
        }
    }

  
}
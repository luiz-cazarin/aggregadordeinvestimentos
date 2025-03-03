package com.example.aggregadordeinvestimentos.service;

import com.example.aggregadordeinvestimentos.controller.dto.CreateAccountDto;
import com.example.aggregadordeinvestimentos.controller.dto.CreateUserDto;
import com.example.aggregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.example.aggregadordeinvestimentos.entity.Account;
import com.example.aggregadordeinvestimentos.entity.Role;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.AccountRepository;
import com.example.aggregadordeinvestimentos.repository.BillingAddressRepository;
import com.example.aggregadordeinvestimentos.repository.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    // Criando um mock do AccountRepository
    @Mock
    private AccountRepository accountRepository;

    // Criando um mock do BillingAddressRepository
    @Mock
    private BillingAddressRepository billingAddressRepository;

    // Injetando o mock do UserRepository no UserService
    @InjectMocks
    private UserService userService;

    // Injetando o mock do AccountService no AccountService
    @InjectMocks
    private AccountService accountService;

    // Criando um captor para capturar o usuário que está sendo salvo
    @Captor
    private ArgumentCaptor<User> userCaptor;

    // Criando um captor para capturar o account que está sendo salvo
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

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
            var role = new Role();
            role.setName(Role.Values.BASIC.name());
            role.setRoleId(1L);

            doReturn(role).when(roleRepository).findByName(Role.Values.BASIC.name());

            var encodedPassword = "1234";
            var password = doReturn(encodedPassword).when(passwordEncoder).encode(encodedPassword);

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    password,
                    Instant.now(),
                    null,
                    new Role()
            );
            // doReturn(user).when(userRepository).save(any()); // any nao e uma boa pratica para cenaarios de sucesso
            doReturn(user).when(userRepository).save(userCaptor.capture()); // passar a estrutura para validar o teste
            var input = new CreateUserDto(
                    "username",
                    "user@mail.com",
                    "1234");
            // Act
            userService.createUser(input);
            // Assert

            var userCaptured = userCaptor.getValue();

            // assertNotNull(output); // no caso de any

            // garantir que o username do usuario capturado é igual ao username do usuario criado
            assertEquals(user.getUsername(), userCaptured.getUsername());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(encodedPassword, userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should throw exception when error occours")
        void shouldThrowExceptionWhenErrorOccours() {
            // Arrange
            var role = new Role();
            role.setName(Role.Values.BASIC.name());

            doReturn(role).when(roleRepository).findByName(Role.Values.BASIC.name());

            var input = new CreateUserDto(
                    "username",
                    "user@mail.com",
                    "1234");

            doThrow(new RuntimeException()).when(userRepository).save(any());
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
                    null,
                    new Role()
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

    @Nested
    class listUsers {
        @Test
        @DisplayName("Should return user list successfully")
        void shouldReturnUserListSuccessfully() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null,
                    new Role()
            );
            doReturn(List.of(user)).when(userRepository).findAll();

            // Act
            var output = userService.getUserList();

            // Assert
            assertNotNull(output);
            assertEquals(1, output.size()); // a lista mocada so tem um usuario enato o tamanho tem que ser 1
        }
    }

    @Nested
    class deleteUserById {
        @Test
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null,
                    new Role()
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidCaptor.capture());
            doNothing().when(userRepository).delete(userCaptor.capture());

            // Act
            userService.deleteUser(user.getUserId().toString());

            // Assert
            var idList = uuidCaptor.getAllValues();
            assertEquals(user.getUserId(), idList.get(0));
            assertEquals(user.getUserId(), userCaptor.getValue().getUserId());
        }

        @Test
        @DisplayName("Should delete user by id successfully when user exists")
        void shouldDeleteUserByIdSuccessfullyWhenUserExists() {
            // Arrange
            doReturn(true).when(userRepository).existsById(uuidCaptor.capture());

            // quando e um metodo sem retorno, como o delete, usamos o doNothing
            doNothing().when(userRepository).deleteById(uuidCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidCaptor.getAllValues(); // por sar o capture duas vezes ele retorna uma lista

            // quando chamamos o existById ele tem q ser o mesmo identificador
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));


            // verificando quantas vezes o metodo foi chamado
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should not delete user witch successfully when user not exists")
        void shouldDeleteUserWitchSuccessfullyWhenUserExists() {
            // Arrange
            doReturn(false).when(userRepository).existsById(uuidCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            assertEquals(userId, uuidCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidCaptor.getValue());

            // certeza que o metodo deleteById nao foi chamado
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUser {
        @Test
        @DisplayName("Should update user successfully when user name and user password exists")
        void shouldUpdateUserSuccessfullyWhenUserNameAndPasswordExists() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null,
                    new Role()
            );


            doReturn(Optional.of(user)).when(userRepository).findById(uuidCaptor.capture());

            doReturn((user)).when(userRepository).save(userCaptor.capture());

            // Act
            userService.updateUser(user.getUserId().toString(), updateUserDto);

            // Assert
            assertEquals(user.getUserId(), uuidCaptor.getValue());

            var userCaptured = userCaptor.getValue();
            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidCaptor.getValue());
            verify(userRepository, times(1)).save(userCaptor.getValue());
        }

        @Test
        @DisplayName("Should not update when user not exists")
        void shouldNotUpdateWhenUserNotExists() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidCaptor.capture());

            // Act
            userService.updateUser(userId.toString(), updateUserDto);

            // Assert
            assertEquals(userId, uuidCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }

    @Nested
    class createAccount {
        @Test
        @DisplayName("Should create account successfully")
        void shouldCreateAccountSuccessfully() {
            var userId = UUID.randomUUID();
            var user = new User(
                    userId,
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null,
                    new Role()
            );

            var accountDto = new CreateAccountDto(
                    "description",
                    "Street Name",
                    123
            );

            doReturn(Optional.of(user)).when(userRepository).findById(userId);
            doReturn(new Account(UUID.randomUUID(), user, null, new ArrayList<>(), accountDto.description()))
                    .when(accountRepository).save(any());

            // Act
            userService.createAccount(userId.toString(), accountDto);

            // Assert
            verify(userRepository, times(1)).findById(userId);
            verify(accountRepository, times(1)).save(accountCaptor.capture());
            verify(billingAddressRepository, times(1)).save(any());
        }

    }

    @Nested
    class getAccounts {
        @Test
        @DisplayName("Should return account list successfully")
        void shouldReturnAccountListSuccessfully() {
            // Arrange
            var userId = UUID.randomUUID();
            var user = new User(
                    userId,
                    "username",
                    "user@mail.com",
                    "1234",
                    Instant.now(),
                    null,
                    new Role()
            );

            var account = new Account(
                    UUID.randomUUID(),
                    user, null,
                    new ArrayList<>(),
                    "description"
            );

            // Associando a conta ao usuário
            user.setAccounts(List.of(account));

            doReturn(Optional.of(user)).when(userRepository).findById(userId);

            // Act
            var output = userService.getAccounts(userId.toString());

            // Assert
            verify(userRepository, times(1)).findById(userId);
            assertNotNull(output);
            assertEquals(1, output.size());
            assertEquals(account.getAccount_id().toString(), output.get(0).account_id());
            assertEquals(account.getDescription(), output.get(0).description());
        }

    }

}
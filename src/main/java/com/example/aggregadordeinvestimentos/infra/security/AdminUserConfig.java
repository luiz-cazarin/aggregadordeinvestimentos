package com.example.aggregadordeinvestimentos.infra.security;

import com.example.aggregadordeinvestimentos.entity.Role;
import com.example.aggregadordeinvestimentos.entity.User;
import com.example.aggregadordeinvestimentos.repository.RoleRepository;
import com.example.aggregadordeinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin user already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setEmail("admin@mail.com");
                    user.setPassword(passwordEncoder.encode("1234"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}















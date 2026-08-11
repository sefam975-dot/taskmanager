package com.project.taskmanager.infrastructure.bootstrap;

import com.project.taskmanager.domain.enums.Role;
import com.project.taskmanager.domain.model.User;
import com.project.taskmanager.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> {
                    user.setRole(Role.ROLE_ADMIN);
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setIsActive(true);
                    user.setUpdatedAt(OffsetDateTime.now());
                    userRepository.save(user);
                },
                () -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    User admin = User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("123"))
                            .role(Role.ROLE_ADMIN)
                            .isActive(true)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    userRepository.save(admin);
                }
        );
    }
}

package com.polstat.sipenat.config;

import com.polstat.sipenat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@polstat.ac.id")
                    .password(passwordEncoder.encode("Admin123!"))
                    .fullName("Admin Polstat")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}

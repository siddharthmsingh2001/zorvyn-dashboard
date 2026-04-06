package com.zorvyn.financedashboard;

import com.zorvyn.financedashboard.entities.Authority;
import com.zorvyn.financedashboard.entities.Role;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.repositories.RoleRepository;
import com.zorvyn.financedashboard.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args){

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role analystRole = createRoleIfNotFound("ROLE_ANALYST");
        Role viewerRole = createRoleIfNotFound("ROLE_VIEWER");

        if (!userRepository.existsByEmailAndIsDeletedFalse("admin@zorvyn.com")) {
            User admin = User.create(
                    "siddharth_admin",
                    "admin@zorvyn.com",
                    passwordEncoder.encode("Admin@123"),
                    adminRole
            );
            admin.setEnabled(true);
            admin.setLocked(false);
            admin.setDeleted(false);

            userRepository.save(admin);
            System.out.println(">> Seed Admin Created: admin@zorvyn.com / Admin@123");
        }

    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role(name);
            if (name.equals("ADMIN")) {
                role.setAuthorities(Set.of(
                        new Authority("WRITE_PRIVILEGE"),
                        new Authority("DELETE_PRIVILEGE")
                ));
            }
            return roleRepository.save(role);
        });
    }

}

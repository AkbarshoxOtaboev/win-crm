package uz.script.wincrm.users;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.script.wincrm.roles.Role;
import uz.script.wincrm.roles.RoleRepository;
import uz.script.wincrm.utils.Status;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(3)
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Username mavjudligini tekshirish
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        // SUPER_ADMIN rolini olish
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

        User user = User.builder()
                .fullName("Super Admin")
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .status(Status.ACTIVE)
                .phone("+998-97-221-88-96")

                .roles(Set.of(superAdminRole))
                .build();

        userRepository.save(user);

        System.out.println("Default SUPER_ADMIN created.");
    }


}

package uz.script.wincrm.roles;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import uz.script.wincrm.permissions.Permissions;
import uz.script.wincrm.permissions.PermissionsRepository;
import uz.script.wincrm.utils.Status;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(2)
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository repository;
    private final PermissionsRepository permissionsRepository;

    @Override
    public void run(String @NonNull ... args) {
        if (repository.count() > 0) {
            return;
        }

        Set<Permissions> permissions = new HashSet<>(permissionsRepository.findAll());
        Role superAdmin = Role.builder()
                .name("SUPER_ADMIN")
                .status(Status.ACTIVE)
                .permissions(permissions)
                .build();

        Role admin = Role.builder()
                .name("ADMIN")
                .status(Status.ACTIVE)
                .permissions(permissions)
                .build();

        repository.save(superAdmin);
        repository.save(admin);

        System.out.println("SUPER_ADMIN role created successfully ✅");

    }
}

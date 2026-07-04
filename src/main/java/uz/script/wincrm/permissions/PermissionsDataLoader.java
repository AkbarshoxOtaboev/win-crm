package uz.script.wincrm.permissions;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import uz.script.wincrm.utils.Action;
import uz.script.wincrm.utils.Resource;
import uz.script.wincrm.utils.Status;

@Component
@RequiredArgsConstructor
@Order(1)
public class PermissionsDataLoader implements CommandLineRunner {

    private final PermissionsRepository permissionsRepository;

    @Override
    public void run(String... args) {
        for (Resource resource : Resource.values()) {
            for (Action action : Action.values()) {

                boolean exists = permissionsRepository
                        .existsByResourceAndAction(resource, action);

                if (!exists) {
                    Permissions permission = Permissions.builder()
                            .resource(resource)
                            .action(action)
                            .name(resource.name() + "_" + action.name())
                            .status(Status.ACTIVE)
                            .build();

                    permissionsRepository.save(permission);
                }
            }
        }

        System.out.println("Permissions generated successfully ✅");
    }
}
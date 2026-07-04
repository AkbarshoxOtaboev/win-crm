package uz.script.wincrm.permissions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.script.wincrm.roles.Role;
import uz.script.wincrm.utils.Action;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.Resource;
import uz.script.wincrm.utils.TableName;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.PERMISSIONS)
public class Permissions extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Resource resource;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}

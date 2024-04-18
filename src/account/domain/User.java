package account.domain;

import account.constants.AcmeRole;
import account.constants.ChangeUserRoleErrorEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Salary> salaries;

    // 하루 간격으로 스케줄러 돌리면서 lockedUntil 풀기
    @Column(nullable = true)
    private LocalDateTime lockedUntil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @OrderBy("name asc")
    private List<Role> roles = new ArrayList<>();

    // domain logic

    @JsonIgnore
    public boolean isAdmin() {
        return this.roles.stream().anyMatch(Role::isAdminRoleGroup);
    }

    public ChangeUserRoleErrorEnum removeRole(Role role) {
        if (role.getName().equals(AcmeRole.ADMINISTRATOR)) {
            return ChangeUserRoleErrorEnum.CANT_REMOVE_ADMIN_ROLE;
        }
        List<Role> filteredList = this.roles.stream().filter(r -> !r.equals(role)).toList();
        // NOTE: do not handle when provided role is not exist
         if (filteredList.size() == this.roles.size()) {
             return ChangeUserRoleErrorEnum.DONT_HAVE_THE_ROLE;
         }
        if (filteredList.isEmpty()) {
            return ChangeUserRoleErrorEnum.HAS_AT_LEAST_ONE_ROLE;
        }
        this.roles.remove(role);
        return null;
    }

    public ChangeUserRoleErrorEnum addRole(Role role) {
        if (this.roles.contains(role)) {
            return ChangeUserRoleErrorEnum.ALREADY_EXISTS;
        }

        Role hasRole  = this.roles.get(0);

        if (hasRole.isAdminRoleGroup() && !role.isAdminRoleGroup()
                || !hasRole.isAdminRoleGroup() && role.isAdminRoleGroup()
        ) {
            return ChangeUserRoleErrorEnum.CANT_HAVE_BOTH_GROUP_ROLE;
        }
        this.roles.add(role);
        Collections.sort(roles, Comparator.comparing(r -> r.getName().name()));
        return null;
    }
}

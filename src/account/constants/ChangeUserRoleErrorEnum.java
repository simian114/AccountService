package account.constants;

import lombok.Getter;

@Getter
public enum ChangeUserRoleErrorEnum {
    NONE("none"),
    ALREADY_EXISTS("already exists"),
    HAS_AT_LEAST_ONE_ROLE("The user must have at least one role!"),
    DONT_HAVE_THE_ROLE("The user does not have a role!"),
    CANT_REMOVE_ADMIN_ROLE("Can't remove ADMINISTRATOR role!"),
    CANT_HAVE_BOTH_GROUP_ROLE("The user cannot combine administrative and business roles!");

    private final String msg;

    ChangeUserRoleErrorEnum(String msg) {
        this.msg = msg;
    }
}


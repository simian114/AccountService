package account.constants;

public enum AcmeRole {
    ANONYMOUS,
    ACCOUNTANT,
    ADMINISTRATOR,
    AUDITOR,
    USER;

    public String getWithPrefix() {
        return "ROLE_" + this.name();
    }
}

package website.fernandoconde.messaging.model;

public enum UserRole {
    ADMIN,
    USER,
    MANAGER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}

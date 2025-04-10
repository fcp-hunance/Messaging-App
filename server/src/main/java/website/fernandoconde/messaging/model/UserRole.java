package website.fernandoconde.messaging.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_MANAGER;

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}

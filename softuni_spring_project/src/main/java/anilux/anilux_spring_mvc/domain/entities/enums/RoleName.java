package anilux.anilux_spring_mvc.domain.entities.enums;

public enum RoleName {
    ROLE_ROOT_ADMIN(0),
    ROLE_ADMIN(1),
    ROLE_USER(2);

    private int authorityLevel;

    RoleName(int authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    public int getAuthorityLevel() {
        return this.authorityLevel;
    }
}

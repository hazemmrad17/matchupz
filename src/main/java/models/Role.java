package models;

public enum Role {
    ADMIN("Admin"),
    RESPONSABLE_SPORTIF("Responsable Sportif"),
    RESERVATEUR("Reservateur"),
    RESPONSABLE_SPONSORING("Responsable Sponsoring"),
    RESPONSABLE_LOGISTIQUE("Responsable Logistique"),
    UTILISATEUR("Utilisateur"),
    RESERVATEURS("Reservateur");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }























    public static Role fromString(String roleStr) {
        for (Role roleEnum : Role.values()) {
            if (roleEnum.getValue().equalsIgnoreCase(roleStr)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + roleStr);
    }
}

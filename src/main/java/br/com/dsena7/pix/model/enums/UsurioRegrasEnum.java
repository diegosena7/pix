package br.com.dsena7.pix.model.enums;

public enum UsurioRegrasEnum {
    USUARIO ("usuario"),
    DEV("dev");

    String role;

    UsurioRegrasEnum(String role) {
        this.role = role;
    }

    String getRole() {
        return role;
    }
}

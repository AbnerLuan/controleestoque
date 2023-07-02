package com.luan.controleestoque.model.Enum;

public enum Profile {

    ADMIN(0, "ROLE_ADMIN"), USER(1, "ROLE_USER");
    private Integer code;
    private String description;

    private Profile(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Profile toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Profile x : Profile.values()) {
            if (cod.equals(x.getCode())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid Profile");
    }

}

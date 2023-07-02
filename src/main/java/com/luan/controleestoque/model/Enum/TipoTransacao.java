package com.luan.controleestoque.model.Enum;

public enum TipoTransacao {

    ENTRADA(0,"ENTRADA"), SAIDA(1,"SAIDA");

    private Integer code;
    private String description;

    private TipoTransacao (Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}

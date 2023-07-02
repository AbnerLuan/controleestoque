package com.luan.controleestoque.model.Enum;

public enum CanalVenda {

    LOJA(0,"LOJA"), WHATSAPP(1,"WHATSAPP"),ML(2,"MERCADO_LIVRE"),
    SHOPEE(3,"SHOPEE"), AMAZON(4,"AMAZON"), OUTROS(5,"OUTROS");

    private Integer code;
    private String description;

    private CanalVenda (Integer code, String description) {
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

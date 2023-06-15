package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private String nomeProduto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    private int quantidade;

    private double valorUnit;

    private double valorTotalItem;

    public ItemPedido( String nomeProduto, Produto produto, Venda venda, int quantidade, double valorUnit) {
        this.itemId = itemId;
        this.nomeProduto = nomeProduto;
        this.produto = produto;
        this.venda = venda;
        this.quantidade = quantidade;
        this.valorUnit = valorUnit;
        calcularValorTotalItem();
    }

    public void calcularValorTotalItem() {
        this.valorTotalItem = quantidade * valorUnit;
    }


}

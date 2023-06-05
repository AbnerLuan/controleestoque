package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    private Produto produto;

    private String nomeProduto;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "venda_Id")
    private Venda venda;

    private int quantidade;

    private double valorUnit;

    private double valorTotalItem;

    public ItemPedido(Long itemId, String nomeProduto, Venda venda,int quantidade, double valorUnit) {
        this.itemId = itemId;
        this.nomeProduto = nomeProduto;
        this.venda = venda;
        this.quantidade = quantidade;
        this.valorUnit = valorUnit;
        this.valorTotalItem = getQuantidade() * getValorUnit();
    }

    public ItemPedido(ItemPedido itemPedido) {
        this.itemId = itemPedido.getItemId();
        this.nomeProduto = itemPedido.getNomeProduto();
        this.venda = itemPedido.getVenda();
        this.quantidade = itemPedido.getQuantidade();
        this.valorUnit = itemPedido.getValorUnit();
        this.valorTotalItem = getQuantidade() * getValorUnit();
    }

//    public ItemPedido(List<ItemPedido> itens) {
//        this.itemId = itens.
//        this.nomeProduto = itens.getNomeProduto();
//        this.venda = itens.getVenda();
//        this.quantidade = itens.getQuantidade();
//        this.valorUnit = itens.getValorUnit();
//        this.valorTotalItem = getQuantidade() * getValorUnit();
//    }
}

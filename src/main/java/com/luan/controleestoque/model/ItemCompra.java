package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemCompraId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "compra_Id")
    private Compra compra;

    @ManyToOne
    private Produto produto;

    private String nomeProduto;

    private int quantidade;

    private double valorUnit;

    private double valorTotalItem;

}

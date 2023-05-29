package com.luan.controleestoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeProduto;

    private String tipoProduto;

    private String marcaProduto;

    private int quantidadeEstoque;

    private double valorUnitario;

    private double valorTotal;

    private int ean;

    private boolean cadastroSite;

    private boolean cadastroMl;

    private boolean cadastroShoppe;

    private boolean blog;

}

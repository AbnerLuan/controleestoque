package com.luan.controleestoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoId;

//    @NotNull(message = "Nome Produto é Obrigatorio.")
    private String nomeProduto;

    @NotNull(message = "Tipo Produto é Obrigatorio.")
    private String tipoProduto;

    @NotNull(message = "Marca Produto é Obrigatorio.")
    private String marcaProduto;

    private int quantidadeEstoque;

    private double valorUnitario;

    private double valorTotal;

    private String ean;

    private boolean cadastroSite;

    private boolean cadastroMl;

    private boolean cadastroShoppe;

    private boolean blog;

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "produto")
    private List<ItemPedido> itens;

    public Produto(Long produtoId, String nomeProduto, String tipoProduto, String marcaProduto, int quantidadeEstoque,
                   double valorUnitario, double valorTotal, String ean, boolean cadastroSite, boolean cadastroMl,
                   boolean cadastroShoppe, boolean blog) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.tipoProduto = tipoProduto;
        this.marcaProduto = marcaProduto;
        this.quantidadeEstoque = quantidadeEstoque;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.ean = ean;
        this.cadastroSite = cadastroSite;
        this.cadastroMl = cadastroMl;
        this.cadastroShoppe = cadastroShoppe;
        this.blog = blog;
    }
}

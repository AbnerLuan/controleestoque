package com.luan.controleestoque.dto;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {
    private Long produtoId;


    private String nomeProduto;

    private String tipoProduto;

    private String marcaProduto;

    private int quantidadeEstoque;

    private double valorUnitario;

    private double valorTotal;

    private String ean;

    private boolean cadastroSite;

    private boolean cadastroMl;

    private boolean cadastroShoppe;

    private boolean blog;

    private List<ItemPedido> itens;


    public ProdutoDTO(Produto produto) {
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
        this.itens = itens;
    }
}

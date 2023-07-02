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

    public ProdutoDTO(Produto produto) {
        this.nomeProduto = produto.getNomeProduto();
        this.tipoProduto = produto.getTipoProduto();
        this.marcaProduto = produto.getMarcaProduto();
        this.quantidadeEstoque = produto.getQuantidadeEstoque();
        this.valorUnitario = produto.getValorUnitario();
        this.valorTotal = produto.getValorTotal();
        this.ean = produto.getEan();
        this.cadastroSite = produto.isCadastroSite();
        this.cadastroMl = produto.isCadastroMl();
        this.cadastroShoppe = produto.isCadastroShoppe();
        this.blog = produto.isBlog();
    }
}

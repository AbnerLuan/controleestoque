package com.luan.controleestoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luan.controleestoque.dto.ProdutoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produtoId")
    private Long produtoId;


    @NotNull(message = "Nome Produto é Obrigatorio.")
    private String nomeProduto;

    @NotNull(message = "Tipo Produto é Obrigatorio.")
    @Column(name = "tipoProduto")
    private String tipoProduto;

    @NotNull(message = "Marca Produto é Obrigatorio.")
    @Column(name = "marcaProduto")
    private String marcaProduto;

    @Column(name = "quantidadeEstoque")
    private int quantidadeEstoque;

    @Column(name = "valorUnitario")
    private double valorUnitario;

    @Column(name = "valorTotal")
    private double valorTotal;

    @Column(name = "ean")
    private String ean;

    @Column(name = "cadastroSite")
    private boolean cadastroSite;

    @Column(name = "cadastroMl" )
    private boolean cadastroMl;

    @Column(name = "cadastroShoppe")
    private boolean cadastroShoppe;

    private boolean blog;

    @JsonIgnore
    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "produto")
    private List<ItemPedido> itens;

    public Produto(String nomeProduto, String tipoProduto, String marcaProduto, int quantidadeEstoque,
                   double valorUnitario, double valorTotal, String ean, boolean cadastroSite, boolean cadastroMl,
                   boolean cadastroShoppe, boolean blog) {
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

    public Produto(ProdutoDTO produtoDTO) {
        this.nomeProduto = produtoDTO.getNomeProduto();
        this.tipoProduto = produtoDTO.getTipoProduto();
        this.marcaProduto = produtoDTO.getMarcaProduto();
        this.quantidadeEstoque = produtoDTO.getQuantidadeEstoque();
        this.valorUnitario = produtoDTO.getValorUnitario();
        this.valorTotal = produtoDTO.getValorTotal();
        this.ean = produtoDTO.getEan();
        this.cadastroSite = produtoDTO.isCadastroSite();
        this.cadastroMl = produtoDTO.isCadastroMl();
        this.cadastroShoppe = produtoDTO.isCadastroShoppe();
        this.blog = produtoDTO.isBlog();
    }
}

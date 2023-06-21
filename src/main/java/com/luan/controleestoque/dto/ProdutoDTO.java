package com.luan.controleestoque.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luan.controleestoque.model.ItemPedido;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProdutoDTO(Long produtoId,
                         @NotNull(message = "Nome Produto é Obrigatorio.") String nomeProduto,
                         @NotNull(message = "Tipo Produto é Obrigatorio.") String tipoProduto,
                         @NotNull(message = "Marca Produto é Obrigatorio.") String marcaProduto,
                         int quantidadeEstoque,
                         double valorUnitario,
                         double valorTotal,
                         String ean,
                         boolean cadastroSite,
                         boolean cadastroShoppe,

                         @JsonIgnore boolean blog, List<ItemPedido> itens) {



}

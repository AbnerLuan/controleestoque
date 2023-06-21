package com.luan.controleestoque.dto;

import com.luan.controleestoque.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(produto.getProdutoId(), produto.getNomeProduto(), produto.getTipoProduto(),
                produto.getMarcaProduto(), produto.getQuantidadeEstoque(), produto.getValorUnitario(),
                produto.getValorTotal(), produto.getEan(), produto.isCadastroSite(), produto.isCadastroShoppe(),
                produto.isBlog(), produto.getItens());
    }
}

package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService (ProdutoRepository produtoRepository) {this.produtoRepository = produtoRepository;}

    public Page<Produto> findAll(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto findById(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        return produtoOptional.orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }


    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto update(Produto produtoNovo, Long id) {
        Produto produtoAntigo = findById(produtoNovo.getProdutoId());
        produtoAntigo.setNomeProduto(produtoNovo.getNomeProduto());
        produtoAntigo.setTipoProduto(produtoNovo.getTipoProduto());
        produtoAntigo.setMarcaProduto(produtoNovo.getMarcaProduto());
        produtoAntigo.setQuantidadeEstoque(produtoNovo.getQuantidadeEstoque());
        produtoAntigo.setValorUnitario(produtoNovo.getValorUnitario());
        produtoAntigo.setValorTotal(produtoNovo.getValorTotal());
        produtoAntigo.setEan(produtoNovo.getEan());
        produtoAntigo.setCadastroSite(produtoAntigo.isCadastroSite());
        produtoAntigo.setCadastroMl(produtoAntigo.isCadastroMl());
        produtoAntigo.setCadastroShoppe(produtoAntigo.isCadastroShoppe());
        produtoAntigo.setBlog(produtoAntigo.isBlog());

        return produtoRepository.save(produtoNovo);
    }

    public List<String> findAllProdutos() {
        return produtoRepository.findAllProdutos();
    }


    public Long findIdByNomeProduto(String nomeProduto) {
        return produtoRepository.findIdByName(nomeProduto);
    }

    public List<Produto> findByName(String nomeProduto) {
        return produtoRepository.findByNomeProdutoIgnoreCaseContaining(nomeProduto);
    }
}

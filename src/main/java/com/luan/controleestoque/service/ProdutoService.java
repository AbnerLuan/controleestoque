package com.luan.controleestoque.service;

import com.luan.controleestoque.dto.ProdutoMapper;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProdutoService {

    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Autowired

    public ProdutoService (ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

//    public Page<ProdutoDTO> findAll(Pageable pageable) {
//        logger.log(Level.INFO, "Lista de Produtos Carregada.");
//        return produtoRepository.findAll(pageable).stream().map(produtoMapper::toDTO)
//               .collect(Collectors.toList());
//    }


    public Page<Produto> findAllPageable(Pageable pageable) {
        logger.log(Level.INFO, "Lista de Produtos Carregada.");
        return produtoRepository.findAll(pageable);
    }
//
//    public List<Produto> findAll() {
//        logger.log(Level.INFO, "Lista de Produtos Carregada.");
//        return produtoRepository.findAll();
//    }


    public Produto findById(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        return produtoOptional.orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }


    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
        logger.log(Level.INFO, "Produto deletado id: " + id);
    }

    public Produto save(Produto produto) {
        produto.setValorTotal(produto.getValorUnitario() * produto.getQuantidadeEstoque());
        logger.log(Level.INFO, "Novo produto Salvo: " + produto.getNomeProduto());
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

    public List<String> findAllNomeProdutos() {
        return produtoRepository.findAllNomeProdutos();
    }


    public Long findIdByNomeProduto(String nomeProduto) {
        return produtoRepository.findIdByName(nomeProduto);
    }

    public List<Produto> findByName(String nomeProduto) {
        return produtoRepository.findByNomeProdutoIgnoreCaseContaining(nomeProduto);
    }

    public Produto findProdutoByName(String nomeProduto) {
        return produtoRepository.findIdByNomeProduto(nomeProduto);
    }

    public List<Produto> findByNomeProdutoIn(Set<String> nomesProdutos) {
        return produtoRepository.findByNomeProdutoIn(nomesProdutos);
    }
}

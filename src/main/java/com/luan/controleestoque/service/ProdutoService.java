package com.luan.controleestoque.service;

import com.luan.controleestoque.dto.ProdutoDTO;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    private final ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Page<ProdutoDTO> findAllPageable(Pageable pageable) {
        logger.log(Level.INFO, "Lista de Produtos Carregada.");
        Page<Produto> produtoPage = produtoRepository.findAll(pageable);

        return produtoPage.map((produto -> modelMapper.map(produto, ProdutoDTO.class)));
    }
    public List<String> findAllNomeProdutos() {
        return produtoRepository.findAllNomeProdutos();
    }
    public List<ProdutoDTO> findByNameIgnoreCase(String nomeProduto) {

        List<Produto> produtos = produtoRepository.findByNomeProdutoIgnoreCaseContaining(nomeProduto);
        return produtos.stream().map((produtoNome -> modelMapper.map(produtoNome, ProdutoDTO.class)))
                .collect(Collectors.toList());
    }
    public Produto findIdProdutoByName(String nomeProduto) {
        return produtoRepository.findIdByNomeProduto(nomeProduto);
    }
    public List<Produto> findByNomeProdutoIn(Set<String> nomesProdutos) {
        return produtoRepository.findByNomeProdutoIn(nomesProdutos);
    }
    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        return produtoOptional.map((produto -> modelMapper.map(produto, ProdutoDTO.class)))
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }
    public ProdutoDTO save(Produto produto) {
        produto.setValorTotal(produto.getValorUnitario() * produto.getQuantidadeEstoque());
        logger.log(Level.INFO, "Novo produto Salvo: " + produto.getNomeProduto());
        Produto salvarProduto = produtoRepository.save(produto);

        return modelMapper.map(salvarProduto, ProdutoDTO.class);
    }
    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {

        ProdutoDTO produtoAntigo = findById(id);
        produtoAntigo = produtoDTO;
        Produto produtoAtualizado = new Produto(produtoAntigo);

        produtoAtualizado.setProdutoId(id);
        produtoRepository.save(produtoAtualizado);
        logger.log(Level.INFO, "Produto de id: " + id + " atualizado com sucesso!");

        return modelMapper.map(produtoAtualizado, ProdutoDTO.class);
    }

    public Produto findProdutoByName(String nomeProduto) {
        return produtoRepository.findIdByNomeProduto(nomeProduto);
    }

    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
        logger.log(Level.INFO, "Produto deletado id: " + id);
    }

    public double obterValorTotalEstoque() {
        return produtoRepository.calcularValorTotalEstoque();
    }
}

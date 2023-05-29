package com.luan.controleestoque.service;

import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService (ProdutoRepository produtoRepository) {this.produtoRepository = produtoRepository;}

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }


    public Produto findById(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        return produtoOptional.orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }


    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }
}

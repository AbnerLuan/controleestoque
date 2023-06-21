package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {this.produtoService = produtoService;}

    @GetMapping
    public ResponseEntity<Page<Produto>> findAll(@PageableDefault(sort = "produtoId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable){
        Page<Produto> produtosPage = produtoService.findAllPageable(pageable);
        return ResponseEntity.ok().body(produtosPage);
    }

    @GetMapping("/nomes")
    public List<String> findAllProdutos() {
        return produtoService.findAllNomeProdutos();
    }

    @GetMapping("/nome/{nomeProduto}")
    public ResponseEntity<List<Produto>> findByName(@PathVariable String nomeProduto) {
        List<Produto> produtos = produtoService.findByName(nomeProduto);
        return ResponseEntity.ok().body(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id) {
        Produto nomeDoProduto = produtoService.findById(id);
        return ResponseEntity.ok().body(nomeDoProduto);
    }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody @Valid Produto produto){
        Produto newObj = produtoService.save(produto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getProdutoId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Produto> update(@RequestBody @Valid Produto produto, @PathVariable Long id){
        return ResponseEntity.ok().body(produtoService.update(produto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> deleteById(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

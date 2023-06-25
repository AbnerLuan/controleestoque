package com.luan.controleestoque.controller;

import com.luan.controleestoque.dto.ProdutoDTO;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }




    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> findAll(@PageableDefault(sort = "produtoId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable){

        Page<ProdutoDTO> produtoDTOPage = produtoService.findAllPageable(pageable);
        return ResponseEntity.ok().body(produtoDTOPage);
    }

    @GetMapping("/nomes")
    public List<String> findAllProdutos() {return produtoService.findAllNomeProdutos();}

    @GetMapping("/nome/{nomeProduto}")
    public ResponseEntity<List<Produto>> findByName(@PathVariable String nomeProduto) {
        List<Produto> produtos = produtoService.findByNameIgnoreCase(nomeProduto);
        return ResponseEntity.ok().body(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> findById(@PathVariable Long id) {
        ProdutoDTO nomeDoProduto = produtoService.findById(id);
        return ResponseEntity.ok().body(nomeDoProduto);
    }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody @Valid Produto produto){
        Produto newObj = produtoService.save(produto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newObj.getProdutoId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProdutoDTO> update(@RequestBody @Valid ProdutoDTO produtoDTO, @PathVariable Long id){
        ProdutoDTO produtoDTOAtualizado = produtoService.update(produtoDTO, id);
        return ResponseEntity.ok().body(produtoDTOAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> deleteById(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

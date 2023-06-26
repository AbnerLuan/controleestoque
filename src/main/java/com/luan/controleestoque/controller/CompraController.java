package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Compra;
import com.luan.controleestoque.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> findById(@PathVariable Long id) {

        return ResponseEntity.ok().body(compraService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Compra>> findAll(@PageableDefault(sort = "compraId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable) {

        Page<Compra> comprasPage = compraService.findAll(pageable);

        return ResponseEntity.ok().body(comprasPage);
    }

    @PostMapping
    public ResponseEntity<Compra> create(@RequestBody Compra compra) {
        return ResponseEntity.ok().body(compraService.save(compra));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Compra> update(@RequestBody Compra compra, @PathVariable Long id) {
        Compra compraAtualizada = compraService.update(compra, id);
        return ResponseEntity.ok().body(compraAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Compra> deleteById(@PathVariable Long id) {
        compraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
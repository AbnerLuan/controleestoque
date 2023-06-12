package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Compra;
import com.luan.controleestoque.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController (CompraService compraService) {this.compraService = compraService;}

    @GetMapping("/{id}")
    public ResponseEntity<Compra> findById(@PathVariable Long id){

        return ResponseEntity.ok().body(compraService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Compra>> findAll() {

        return ResponseEntity.ok().body(compraService.findAll());
    }

    @PostMapping
    public ResponseEntity<Compra> create(@RequestBody Compra compra) {
        return ResponseEntity.ok().body(compraService.save(compra));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Compra> update(@RequestBody Compra compra, @PathVariable Long id){
        return ResponseEntity.ok().body(compraService.update(compra, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Compra> deleteById(@PathVariable Long id) {return ResponseEntity.noContent().build();}
}

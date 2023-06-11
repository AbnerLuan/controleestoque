package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Venda;
import com.luan.controleestoque.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService vendaService;

    @Autowired
    public VendaController(VendaService vendaService) {this.vendaService = vendaService;}

    @GetMapping("/{id}")
    public ResponseEntity<Venda> findByID(@PathVariable Long id) {
        return ResponseEntity.ok().body(vendaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Venda>> findAll(){

        return ResponseEntity.ok().body(vendaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Venda> create(@RequestBody Venda venda){
        return ResponseEntity.ok().body(vendaService.save(venda));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Venda> update(@RequestBody Venda venda, @PathVariable Long id) {
        Venda vendaAtualizada = vendaService.update(venda, id);
        return ResponseEntity.ok().body(vendaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venda> deleteByID(@PathVariable Long id) {
        vendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

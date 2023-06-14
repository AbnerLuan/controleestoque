package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Venda;
import com.luan.controleestoque.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
    public ResponseEntity<Page<Venda>> findAll(@PageableDefault(sort = "vendaId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable){

        Page<Venda> vendasPage = vendaService.findAll(pageable);

        return ResponseEntity.ok().body(vendasPage);
    }

    @PostMapping
    public ResponseEntity<Venda> create(@RequestBody Venda venda){
        return ResponseEntity.ok().body(vendaService.save(venda));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Venda> update(@PathVariable Long id, @RequestBody Venda venda) {
        Venda vendaAtualizada = vendaService.update(id, venda);
        return ResponseEntity.ok().body(vendaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venda> deleteByID(@PathVariable Long id) {
        vendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

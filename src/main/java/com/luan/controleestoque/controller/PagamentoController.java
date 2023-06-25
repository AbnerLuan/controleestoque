package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Pagamento;
import com.luan.controleestoque.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<Pagamento> create(@RequestBody @Valid Pagamento pagamento) {
        Pagamento newObj = pagamentoService.save(pagamento);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getPagamentoId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pagamento> delete(@PathVariable Long id) {
        pagamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

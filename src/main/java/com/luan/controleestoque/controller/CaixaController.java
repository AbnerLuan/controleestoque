package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.Caixa;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.service.CaixaService;
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

@RestController
@RequestMapping("/api/caixa")
public class CaixaController {

    private final CaixaService caixaService;

    @Autowired
    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping
    public ResponseEntity<Page<Caixa>> findAll(@PageableDefault(sort = "caixaId",
            direction = Sort.Direction.DESC,
            page = 0,
            size = 10) Pageable pageable) {
        Page<Caixa> caixaPage = caixaService.findAllPageable(pageable);
        return ResponseEntity.ok().body(caixaPage);
    }

    @PostMapping
    public ResponseEntity<Caixa> transacaoNoCaixa(@RequestBody @Valid Caixa caixa) {
        Caixa newObj = caixaService.save(caixa);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getCaixaId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}

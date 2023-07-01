package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.ItemCompra;
import com.luan.controleestoque.service.ItemCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/itemcompra")
public class ItemCompraController {

    private final ItemCompraService itemCompraService;

    @Autowired
    public ItemCompraController(ItemCompraService itemCompraService) {
        this.itemCompraService = itemCompraService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemCompra> deleteById(@PathVariable Long id) {
        itemCompraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

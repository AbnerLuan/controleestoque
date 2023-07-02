package com.luan.controleestoque.controller;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.repository.ItemPedidoRepository;
import com.luan.controleestoque.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/itempedido")
public class ItemPedidoController {
    private final ItemPedidoService itemPedidoService;
    @Autowired
    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemPedido> deleteById(@PathVariable Long id) {
        itemPedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

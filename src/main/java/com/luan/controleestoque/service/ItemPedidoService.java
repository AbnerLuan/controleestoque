package com.luan.controleestoque.service;

import com.luan.controleestoque.model.ItemPedido;
import com.luan.controleestoque.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository){
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public void delete(ItemPedido itemAntigo) {
        itemPedidoRepository.delete(itemAntigo);
    }
}

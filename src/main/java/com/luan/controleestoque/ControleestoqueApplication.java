package com.luan.controleestoque;

import com.luan.controleestoque.model.*;
import com.luan.controleestoque.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class ControleestoqueApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ControleestoqueApplication.class, args);
    }


    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private ItemCompraRepository itemCompraRepository;

    @Override
    public void run(String... args) throws Exception {
        Produto p1 = new Produto(null,"Produto1", "Celular", "Apple", 15, 7.890, 118.350, 1, true, true, true, true);
        Produto p2 = new Produto(null,"Produto2", "Caderno", "Tilibra", 150, 10, 1.500, 2, true, true, true, true);
        Produto p3 = new Produto(null,"Produto3", "Vídeo-Game", "Sony", 5, 5.000, 25.000, 3, true, true, true, true);
        Produto p4 = new Produto(null,"Produto4", "Televisão", "Samsung", 14, 2.500, 35.000, 4, true, true, true, true);
        Produto p5 = new Produto(null,"Produto5", "Ventilador", "Arno", 20, 140.00, 2.800, 5, true, true, true, true);

        Venda v1 = new Venda(null,"João", "2", 0, LocalDate.now());
        Venda v2 = new Venda(null, "Lucas", "12", 0, LocalDate.now());
        Venda v3 = new Venda(null,"Fernanda", "7", 0, LocalDate.now());
        Venda v4 = new Venda(null, "Joana", "3", 0, LocalDate.now());
        Venda v5 = new Venda(null, "Maria", "5", 0, LocalDate.now());

        ItemPedido itemPedido1 = new ItemPedido(null,p5.getNomeProduto(),v1,2, 550.00);
        ItemPedido itemPedido2 = new ItemPedido(null, p1.getNomeProduto(),v3, 1, 700.00);
        ItemPedido itemPedido3 = new ItemPedido(null, p4.getNomeProduto(),v5, 10, 40.00);
        ItemPedido itemPedido4 = new ItemPedido(null, p3.getNomeProduto(),v2, 11, 230.00);
        ItemPedido itemPedido5 = new ItemPedido(null, p2.getNomeProduto(),v4, 5, 100.00);

        Compra c1 = new Compra(null, "Jequiti", "1", 5.500, LocalDate.now());
        Compra c2 = new Compra(null, "Nestle", "2", 15.700, LocalDate.now());
        Compra c3 = new Compra(null, "Nike", "3", 130.000, LocalDate.now());
        Compra c4 = new Compra(null, "Adidas", "4", 130.500, LocalDate.now());
        Compra c5 = new Compra(null, "Ferrari", "5", 1.5, LocalDate.now());

        ItemCompra itemCompra1 = new ItemCompra(null, p1.getNomeProduto(), c1, 5, 50.00);
        ItemCompra itemCompra2 = new ItemCompra(null, p2.getNomeProduto(), c2, 2, 500.00);
        ItemCompra itemCompra3 = new ItemCompra(null, p3.getNomeProduto(), c3, 7, 20.00);
        ItemCompra itemCompra4 = new ItemCompra(null, p4.getNomeProduto(), c4, 4, 70.00);
        ItemCompra itemCompra5 = new ItemCompra(null, p5.getNomeProduto(), c5, 5, 55.00);



        vendaRepository.saveAll(Arrays.asList(v1,v2,v3,v4,v5));
        produtoRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5));
        itemPedidoRepository.saveAll(Arrays.asList(itemPedido1,itemPedido2, itemPedido3, itemPedido4, itemPedido5));
        compraRepository.saveAll(Arrays.asList(c1,c2,c3,c4,c5));
        itemCompraRepository.saveAll(Arrays.asList(itemCompra1, itemCompra2, itemCompra3, itemCompra4, itemCompra5));
    }
}

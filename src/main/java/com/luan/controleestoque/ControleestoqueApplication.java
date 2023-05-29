package com.luan.controleestoque;

import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ControleestoqueApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ControleestoqueApplication.class, args);
    }


    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) throws Exception {
        Produto p1 = new Produto(null,"Produto1", "Celular", "Apple", 15, 7.890, 118.350, 1, true, true, true, true);
        Produto p2 = new Produto(null,"Produto2", "Caderno", "Tilibra", 150, 10, 1.500, 2, true, true, true, true);
        Produto p3 = new Produto(null,"Produto3", "Vídeo-Game", "Sony", 5, 5.000, 25.000, 3, true, true, true, true);
        Produto p4 = new Produto(null,"Produto4", "Televisão", "Samsung", 14, 2.500, 35.000, 4, true, true, true, true);
        Produto p5 = new Produto(null,"Produto5", "Ventilador", "Arno", 20, 140.00, 2.800, 5, true, true, true, true);

    produtoRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5));
    }
}

package com.luan.controleestoque.repository;


import com.luan.controleestoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p.nomeProduto FROM Produto p")
    List<String> findAllNomeProdutos();

    @Query("SELECT p.produtoId FROM Produto p WHERE p.nomeProduto = :nomeProduto")
    Long findIdByName(String nomeProduto);

    List<Produto> findByNomeProdutoIgnoreCaseContaining(String nomeProduto);

    Produto findIdByNomeProduto(String nomeProduto);

    List<Produto> findByNomeProdutoIn(Set<String> nomesProdutos);

    @Query("SELECT SUM(p.valorTotal) FROM Produto p")
    double calcularValorTotalEstoque();
}

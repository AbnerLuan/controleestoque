package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.model.dto.ProdutoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p.nomeProduto FROM Produto p")
    List<String> findAllProdutos();

}

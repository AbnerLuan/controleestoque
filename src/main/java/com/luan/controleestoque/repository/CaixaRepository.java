package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    @Query("SELECT c.saldoCaixa FROM Caixa c ORDER BY c.id DESC LIMIT 1")
    double findUltimoValorSaldo();
}

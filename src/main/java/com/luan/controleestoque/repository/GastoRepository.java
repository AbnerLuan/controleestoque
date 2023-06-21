package com.luan.controleestoque.repository;

import com.luan.controleestoque.model.Gasto;
import com.luan.controleestoque.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

}

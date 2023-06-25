package com.luan.controleestoque.service;

import com.luan.controleestoque.dto.ProdutoDTO;
import com.luan.controleestoque.model.Produto;
import com.luan.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProdutoService {

    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    private final ProdutoRepository produtoRepository;


    @Autowired
    public ProdutoService (ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }


    public Page<ProdutoDTO> findAllPageable(Pageable pageable) {
        logger.log(Level.INFO, "Lista de Produtos Carregada.");
        Page<Produto> produtoPage = produtoRepository.findAll(pageable);
        Page<ProdutoDTO> produtosPageDTO = produtoPage.map(ProdutoDTO::new);

        return produtosPageDTO;
    }

    public List<String> findAllNomeProdutos() {
        return produtoRepository.findAllNomeProdutos();
    }

    public List<Produto> findByNameIgnoreCase(String nomeProduto) {
        return produtoRepository.findByNomeProdutoIgnoreCaseContaining(nomeProduto);
    }

    public Produto findIdProdutoByName(String nomeProduto) {
        return produtoRepository.findIdByNomeProduto(nomeProduto);
    }

    public List<Produto> findByNomeProdutoLista(Set<String> nomesProdutos) {
        return produtoRepository.findByNomeProdutoLista(nomesProdutos);
    }

    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        Optional<ProdutoDTO> produtoDTOOptional = produtoOptional.map(ProdutoDTO::new);
        return produtoDTOOptional.orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }

    public Produto save(Produto produto) {
        produto.setValorTotal(produto.getValorUnitario() * produto.getQuantidadeEstoque());
        logger.log(Level.INFO, "Novo produto Salvo: " + produto.getNomeProduto());
        return produtoRepository.save(produto);
    }
    public ProdutoDTO update(ProdutoDTO produtoDTONovo, Long id) {
        ProdutoDTO produtoAntigo = findById(id);
        produtoAntigo.setNomeProduto(produtoDTONovo.getNomeProduto());
        produtoAntigo.setTipoProduto(produtoDTONovo.getTipoProduto());
        produtoAntigo.setMarcaProduto(produtoDTONovo.getMarcaProduto());
        produtoAntigo.setQuantidadeEstoque(produtoDTONovo.getQuantidadeEstoque());
        produtoAntigo.setValorUnitario(produtoDTONovo.getValorUnitario());
        produtoAntigo.setValorTotal(produtoDTONovo.getValorTotal());
        produtoAntigo.setEan(produtoDTONovo.getEan());
        produtoAntigo.setCadastroSite(produtoDTONovo.isCadastroSite());
        produtoAntigo.setCadastroMl(produtoDTONovo.isCadastroMl());
        produtoAntigo.setCadastroShoppe(produtoDTONovo.isCadastroShoppe());
        produtoAntigo.setBlog(produtoDTONovo.isBlog());

        Produto produtoNovo = produtoRepository.save(produtoDTONovo);


        return produtoDTONovo;

//        Page<Produto> produtoPage = produtoRepository.findAll(pageable);
//        Page<ProdutoDTO> produtosPageDTO = produtoPage.map(ProdutoDTO::new);
    }

    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
        logger.log(Level.INFO, "Produto deletado id: " + id);
    }
}

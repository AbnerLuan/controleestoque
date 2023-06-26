package com.luan.controleestoque.service;

import com.luan.controleestoque.model.*;
import com.luan.controleestoque.model.Enum.TipoTransacao;
import com.luan.controleestoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private static final Logger logger = Logger.getLogger(VendaService.class.getName());

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;

    private final ItemPedidoService itemPedidoService;

    private final CaixaService caixaService;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoService produtoService,
                        ItemPedidoService itemPedidoService, CaixaService caixaService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
        this.caixaService = caixaService;
    }

    public Page<Venda> findAll(Pageable pageable) {
        Page<Venda> vendas = vendaRepository.findAll(pageable);
        logger.log(Level.INFO, "Lista de vendas carregada.");
        return vendas;
    }

    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda nao encontrada"));
    }

    public void deleteById(Long id) {
        Optional<Venda> optionalVenda = vendaRepository.findById(id);
        if (optionalVenda.isPresent()) {
            Venda venda = optionalVenda.get();
            List<ItemPedido> itensPedido = venda.getItens();
            for (ItemPedido itemPedido : itensPedido) {
                itemPedidoService.deleteById(itemPedido.getItemId());
            }
            vendaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venda não encontrada");
        }
    }

    public Venda save(Venda venda) {
        Venda vendaSalva = new Venda(venda);
        vendaSalva.setDataVenda(LocalDate.now());
        Map<String, Produto> nomeParaProdutoMap = buscarProdutosPorNome(venda.getItens());
        if (verificarQuantidadeProdutos(venda.getItens(), nomeParaProdutoMap)) {
            throw new RuntimeException("Quantidade insuficiente de produtos no estoque.");
        }
        List<ItemPedido> itensAtualizados = prepararItensSave(venda.getItens(), vendaSalva, nomeParaProdutoMap);
        double valorTotalVenda = calcularValorTotalVenda(itensAtualizados);
        vendaSalva.setValorTotalVenda(valorTotalVenda);
        double lucro = calcularLucroVenda(vendaSalva, itensAtualizados);
        vendaSalva.setLucroVenda(lucro);
        reduzirQuantidadeProdutosEstoque(itensAtualizados);
        vendaSalva.setItens(itensAtualizados);
        vendaRepository.save(vendaSalva);
        logger.log(Level.INFO, "Venda Salva com sucesso.");
        criarLancamentoCaixa(vendaSalva);
        return vendaSalva;
    }

    private int calcularNovaQuantidade(int quantidadeEstoque, int quantidadeVendida) {
        return quantidadeEstoque - quantidadeVendida;
    }

    private List<ItemPedido> prepararItensSave(List<ItemPedido> itens, Venda vendaSalva, Map<String, Produto> nomeParaProdutoMap) {
        return itens.stream()
                .peek(item -> item.setVenda(vendaSalva))
                .peek(item -> {
                    Optional<Produto> optionalProduto = Optional.ofNullable(nomeParaProdutoMap.get(item.getNomeProduto()));
                    optionalProduto.ifPresent(item::setProduto);
                    item.calcularValorTotalItem();
                })
                .collect(Collectors.toList());
    }

    private double calcularLucroVenda(Venda venda, List<ItemPedido> itens) {
        double custoTotalDosProdutos = calcularCustoTotalDosProdutos(itens);
        return venda.getValorTotalVenda() - venda.getValorFrete() - venda.getValorTarifa() - custoTotalDosProdutos;
    }

    private double calcularCustoTotalDosProdutos(List<ItemPedido> itens) {
        return itens.stream()
                .mapToDouble(item -> {
                    Optional<Produto> optionalProduto = Optional.ofNullable(item.getProduto());
                    return optionalProduto.map(produto -> produto.getValorUnitario() * item.getQuantidade()).orElse(0.0);
                })
                .sum();
    }

    private double calcularValorTotalVenda(List<ItemPedido> itens) {
        return itens.stream()
                .mapToDouble(ItemPedido::getValorTotalItem)
                .sum();
    }

    private Map<String, Produto> buscarProdutosPorNome(List<ItemPedido> itens) {
        Set<String> nomesProdutos = itens.stream()
                .map(ItemPedido::getNomeProduto)
                .collect(Collectors.toSet());

        List<Produto> produtos = produtoService.findByNomeProdutoIn(nomesProdutos);

        return produtos.stream()
                .collect(Collectors.toMap(Produto::getNomeProduto, Function.identity()));
    }

    private boolean verificarQuantidadeProdutos(List<ItemPedido> itens, Map<String, Produto> nomeParaProdutoMap) {
        for (ItemPedido item : itens) {
            Optional<Produto> optionalProduto = Optional.ofNullable(nomeParaProdutoMap.get(item.getNomeProduto()));
            if (optionalProduto.isPresent() && item.getQuantidade() > optionalProduto.get().getQuantidadeEstoque()) {
                return true;
            }
        }
        return false;
    }

    private void atualizarQuantidadeProdutoEstoque(Produto produto, int novaQuantidade) {
        produto.setQuantidadeEstoque(novaQuantidade);
        produtoService.save(produto);
    }

    private void reduzirQuantidadeProdutosEstoque(List<ItemPedido> itens) {
        itens.forEach(item -> {
            Produto produto = item.getProduto();
            int novaQuantidade = calcularNovaQuantidade(produto.getQuantidadeEstoque(), item.getQuantidade());
            atualizarQuantidadeProdutoEstoque(produto, novaQuantidade);
        });
    }

    public Venda update(Long id, Venda venda) {
        Venda vendaAntiga = findById(id);
        Venda vendaAtualizada = new Venda(venda);
        vendaAtualizada.setVendaId(id);
        prepararItensUpdate(vendaAntiga, vendaAtualizada, vendaAtualizada);
        vendaAtualizada.setValorTotalVenda(calcularValorTotalVenda(vendaAtualizada.getItens()));
        vendaAtualizada.setLucroVenda(calcularLucroVenda(vendaAtualizada, vendaAtualizada.getItens()));
        logger.log(Level.INFO, "Venda atualizada com sucesso.");
        return vendaRepository.save(vendaAtualizada);
    }

    private void prepararItensUpdate(Venda vendaAntiga, Venda vendaNova, Venda vendaAtualizada) {
        List<ItemPedido> listaItensAtualizados = vendaNova.getItens();
        List<ItemPedido> listaItensAntigos = vendaAntiga.getItens();

        Map<String, Produto> nomeParaProdutoMap = buscarProdutosPorNome(vendaNova.getItens());
        if (!calcularDiferencaDeQuantidadeProdutoVerificaEAtualiza(listaItensAntigos, listaItensAtualizados, nomeParaProdutoMap)) {
            throw new RuntimeException("Quantidade insuficiente de produtos no estoque.");
        }
        for (ItemPedido itemAtualizado : listaItensAtualizados) {
            Produto produto = nomeParaProdutoMap.get(itemAtualizado.getNomeProduto());
            if (produto != null) {
                itemAtualizado.setProduto(produto);
                itemAtualizado.setVenda(vendaAtualizada);
                itemAtualizado.calcularValorTotalItem();
            }
        }

    }

    private boolean calcularDiferencaDeQuantidadeProdutoVerificaEAtualiza(List<ItemPedido> listaItensAntigos,
                                                                          List<ItemPedido> listaItensAtualizados,
                                                                          Map<String, Produto> nomeParaProdutoMap) {
        List<ItemPedido> listaParaVerificarQuantidadeEAtualizar = new ArrayList<>();

        for (ItemPedido itemVendaNova : listaItensAtualizados) {
            if (itemVendaNova.getItemId() == null) {

                listaParaVerificarQuantidadeEAtualizar.add(new ItemPedido(itemVendaNova));
            } else {
                for (ItemPedido itemVendaAntiga : listaItensAntigos) {
                    if (itemVendaNova.getItemId().equals(itemVendaAntiga.getItemId()) &&
                            itemVendaNova.getQuantidade() != itemVendaAntiga.getQuantidade()) {

                        int quantidadeAtualizada = itemVendaNova.getQuantidade() - itemVendaAntiga.getQuantidade();
                        ItemPedido itemCopia = new ItemPedido(itemVendaNova);
                        itemCopia.setQuantidade(quantidadeAtualizada);
                        listaParaVerificarQuantidadeEAtualizar.add(itemCopia);
                        break;
                    }
                }
            }
        }

        if (verificarQuantidadeProdutos(listaParaVerificarQuantidadeEAtualizar, nomeParaProdutoMap)) {
            throw new RuntimeException("Quantidade insuficiente de produtos no estoque.");
        } else {
            reduzirQuantidadeProdutosEstoqueUpdate(listaParaVerificarQuantidadeEAtualizar, nomeParaProdutoMap);
            return true;
        }
    }

    private void reduzirQuantidadeProdutosEstoqueUpdate(List<ItemPedido> listaParaVerificarQuantidadeEAtualizar,
                                                        Map<String, Produto> nomeParaProdutoMap) {
        for (ItemPedido item : listaParaVerificarQuantidadeEAtualizar) {
            Produto produto = nomeParaProdutoMap.get(item.getNomeProduto());
            if (produto != null) {
                int novaQuantidade = calcularNovaQuantidade(produto.getQuantidadeEstoque(), item.getQuantidade());
                atualizarQuantidadeProdutoEstoque(produto, novaQuantidade);
            }
        }
    }

    public double obterValorTotalFaturamentoAnual() {
        return vendaRepository.calculaValorTotalFaturamentoAnual();
    }

    public double obterValorFaturamentoMesAtual() {
        return vendaRepository.calculaValorTotalFaturamentoMesAtual();
    }

    public double obterValorTotalLucroAnual() {
        return vendaRepository.calculaValorTotalLucroAnual();
    }

    public double obterValorLucroMesAtual() {
        return vendaRepository.calculaValorTotalLucroMesAtual();
    }

    public List<Double> getValoresFaturamentoMensalFromDatabase() {
        List<MesValor> mesValores = vendaRepository.getValoresFaturamentoMensal();
        List<Double> valoresFaturamentoMensal = new ArrayList<>(Collections.nCopies(12, 0.0));
        for (MesValor mesValor : mesValores) {
            int monthIndex = mesValor.getMes() - 1;
            valoresFaturamentoMensal.set(monthIndex, mesValor.getValor());
        }
        return valoresFaturamentoMensal;
    }

    public List<Double> getValoresLucroMensalFromDatabase() {
        List<MesValor> mesValores = vendaRepository.getValoresLucroMensalFromDataBase();
        List<Double> valoresLucroMensal = new ArrayList<>(Collections.nCopies(12, 0.0));
        for (MesValor mesValor : mesValores) {
            int monthIndex = mesValor.getMes() - 1;
            valoresLucroMensal.set(monthIndex, mesValor.getValor());
        }
        return valoresLucroMensal;
    }

    public void criarLancamentoCaixa(Venda venda) {
        Caixa lancamentoVendaCaixa = new Caixa();
        lancamentoVendaCaixa.setValorTransacao(calculaValorVendaLiquido(venda));
        lancamentoVendaCaixa.setTipoTransacao(TipoTransacao.ENTRADA);
        lancamentoVendaCaixa.setObservacao("Lancamento nova venda de ID: " + venda.getVendaId());
        caixaService.save(lancamentoVendaCaixa);
    }

    public double calculaValorVendaLiquido(Venda venda) {
        return venda.getValorTotalVenda() - venda.getValorTarifa() - venda.getValorFrete();
    }

}







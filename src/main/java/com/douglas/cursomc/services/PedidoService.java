package com.douglas.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglas.cursomc.domain.ItemPedido;
import com.douglas.cursomc.domain.PagamentoComBoleto;
import com.douglas.cursomc.domain.Pedido;
import com.douglas.cursomc.domain.enums.EstadoPagamento;
import com.douglas.cursomc.repositories.ItemPedidoRepository;
import com.douglas.cursomc.repositories.PagamentoRepository;
import com.douglas.cursomc.repositories.PedidoRepository;
import com.douglas.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	@Autowired
	private ProdutoService produtoService;

	public Pedido find(Integer id) {

		Optional<Pedido> obj = pedidoRepository.findById(id);

		return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class));

	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip: obj.getItens()) {
			
			ip.setDesconto(0.0);
			ip.setPedido(obj);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
	}

}

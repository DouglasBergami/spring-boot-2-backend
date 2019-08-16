package com.douglas.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglas.cursomc.domain.Categoria;
import com.douglas.cursomc.domain.Cliente;
import com.douglas.cursomc.domain.ItemPedido;
import com.douglas.cursomc.domain.PagamentoComBoleto;
import com.douglas.cursomc.domain.Pedido;
import com.douglas.cursomc.domain.enums.EstadoPagamento;
import com.douglas.cursomc.repositories.ItemPedidoRepository;
import com.douglas.cursomc.repositories.PagamentoRepository;
import com.douglas.cursomc.repositories.PedidoRepository;
import com.douglas.cursomc.security.UserSS;
import com.douglas.cursomc.services.exception.AuthorizationException;
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
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {

		Optional<Pedido> obj = pedidoRepository.findById(id);

		return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class));

	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		for (ItemPedido ip : obj.getItens()) {

			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPedido(obj);
			ip.setPreco(ip.getProduto().getPreco());
		}

		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		UserSS user = UserService.authenticated();
		
		if (user==null) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}

}

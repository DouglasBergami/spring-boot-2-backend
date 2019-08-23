package com.douglas.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.douglas.cursomc.domain.Cidade;
import com.douglas.cursomc.domain.Estado;
import com.douglas.cursomc.dto.CidadeDTO;
import com.douglas.cursomc.dto.EstadoDTO;
import com.douglas.cursomc.services.CidadeService;
import com.douglas.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;
	@Autowired
	private CidadeService cidadeService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> lista = estadoService.findAll();

		List<EstadoDTO> listaDto = lista.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listaDto);
	}

	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidade(@PathVariable Integer estadoId) {
		List<Cidade> listaCidade = cidadeService.findCidadesPorEstado(estadoId);

		List<CidadeDTO> listaCidadeDto = listaCidade.stream().map(obj -> new CidadeDTO(obj))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(listaCidadeDto);
	}

}

package com.douglas.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.douglas.cursomc.domain.Categoria;
import com.douglas.cursomc.domain.Produto;
import com.douglas.cursomc.repositories.CategoriaRepositoy;
import com.douglas.cursomc.repositories.ProdutoRepository;
import com.douglas.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CategoriaRepositoy categoriaRepositoy;

	public Produto find(Integer id) {

		Optional<Produto> obj = produtoRepository.findById(id);

		return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class));

	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> listaCategoria = categoriaRepositoy.findAllById(ids);
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, listaCategoria, pageRequest);
	}
}

package com.douglas.cursomc.repositories;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.douglas.cursomc.domain.Categoria;
import com.douglas.cursomc.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{	

	/*@Query("SELECT DISTINCT obj "
			+ "FROM Produto obj "
			+ "INNER JOIN obj.categorias cat "
			+ "WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);*/
	
	//pode ser trocado a consulta atráves do metodo abaixo, pois pelo spring ele já identifica esse tipo de query
	
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

}

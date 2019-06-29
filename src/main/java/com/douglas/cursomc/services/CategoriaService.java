package com.douglas.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.douglas.cursomc.domain.Categoria;
import com.douglas.cursomc.repositories.CategoriaRepositoy;
import com.douglas.cursomc.services.exception.DataIntegrityException;
import com.douglas.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepositoy repository;

	public Categoria find(Integer id) {

		Optional<Categoria> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));

	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repository.save(obj);
	}

	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repository.save(obj);
	}

	public void delete(Integer id) {
		find(id);
		try{
			repository.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
		
	}

}

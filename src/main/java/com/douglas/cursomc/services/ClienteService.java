package com.douglas.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglas.cursomc.domain.Cliente;
import com.douglas.cursomc.repositories.ClienteRepository;
import com.douglas.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente find(Integer id) {

		Optional<Cliente> obj = clienteRepository.findById(id);

		return obj.orElseThrow(
				() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class));

	}

}

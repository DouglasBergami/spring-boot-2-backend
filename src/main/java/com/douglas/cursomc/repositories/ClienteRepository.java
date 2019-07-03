package com.douglas.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.douglas.cursomc.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
	Cliente findByEmail(String email);

}

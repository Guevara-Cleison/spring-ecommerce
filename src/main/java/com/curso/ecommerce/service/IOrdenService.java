package com.curso.ecommerce.service;

import java.util.List;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	
	Orden save(Orden orden);
	
	List<Orden> findAll();
	
	String generarNumeroOrden();
	
	List<Orden> findByUsuario(Usuario usuario);

}

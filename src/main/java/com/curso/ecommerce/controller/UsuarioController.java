package com.curso.ecommerce.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger LOGGER= LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	//MOSTRAR PAGINA REGISTRO
	@GetMapping("/registro")
	public String create() {
		 
		return "usuario/registro";
	}
	
	//METODO PARA GUARDAR USUARIO
	@PostMapping("/save")
	public String save(Usuario usuario) {
		
		//LOGGER.info("Usuario registro: {} " ,usuario);
		usuario.setTipo("USER");
		
		usuarioService.save(usuario);
		
		return "redirect:/";
	}
	
	//MOSTRAR LA PAGINA DE LOGIN
	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		//LOGGER.info("Accesos : {} ", usuario);
		
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		//LOGGER.info("USUARIO OBTENIDO: {} ", user.get());
		
		//
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
		}else {
			LOGGER.info("Usuario no existe");
		}
		
		return "redirect:/";
	}
	
}

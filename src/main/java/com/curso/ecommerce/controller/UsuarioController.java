package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger LOGGER= LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
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
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()) );
		usuarioService.save(usuario);
		
		return "redirect:/";
	}
	
	//MOSTRAR LA PAGINA DE LOGIN
	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}
	
	//PARA IDENTIFICAR QUE TIPO DE USUARIO ES
	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		//LOGGER.info("Accesos : {} ", usuario);
		
		Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() ) );
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
	
	//MOSTRAR LAS COMPRAS DEL USUARIO
	@GetMapping("/compras")
	public String obtenerCompras(Model model ,HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		
		return "usuario/compras";
	}
	
	
	//MOSTRAR LAS COMPRAS DEL USUARIO
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		LOGGER.info("ID de la orden: {}", id);
		
		Optional<Orden> orden = ordenService.findById(id);
		//JPA NOS PERMITE TRAER LOS DETALLES A TRAVES DE LA ORDEN GRACIAS A LA RELACION DE ENTIDADES
		model.addAttribute("detalles", orden.get().getDetalle() );
		
		//SESSION
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/detalleCompra";
	}
	
	//CERRA SESSION
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		
		session.removeAttribute("idusuario");
		
		return "redirect:/";
	}
	
	
}

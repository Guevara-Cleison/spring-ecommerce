package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger LOGGER= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	//Para_almacenar_los_detalles_de_las_orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//Datos_de_la_orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		
		LOGGER.info("Sesion ----------------- del usuario: {} ", session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findAll());
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/home";
	}
	
	//MOSTRAR PRODUCTO EN LA VISTA PRODUCTOHOME
	@GetMapping("/productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		//LOGGER.info("ID producto enviado como paramaetro: {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
	//METODO PARA AGREGAR PRODUCTOS AL CARRITO
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model ) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		//LOGGER.info("Producto añadido: {}", optionalProducto.get());
		//LOGGER.info("Cantidad: {}", cantidad);
		//Obtenemos_el_producto
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		//Validar_que_el_producto_no_se_añada_dos_veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if(!ingresado) {
		detalles.add(detalleOrden);
		}
		
		//Suma_total_de_los_productos
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Quitar_un_producto_del_carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCcart(@PathVariable Integer id, Model model) {
		
		//Lista_nueva_de_productos
		List<DetalleOrden> ordenenNueva = new ArrayList<DetalleOrden>();
		
		for (DetalleOrden detalleOrden : detalles) {
			if(detalleOrden.getProducto().getId() != id) {
				ordenenNueva.add(detalleOrden);
			}
		}
		//Poner_la_nueva_lista_con_los_productos_restantes
		detalles = ordenenNueva;
		
		//Suma_total_de_los_productos
		double sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
				
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//MOSTRAR LA PAGINA CARRITO
	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		//SESION
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/carrito";
	}
	
	//ENVIAR Y MOSTRAR LA PAGINA DE RESUMEN DE LA ORDEN
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		//OBTIENE EL USUARIO DE LA SESION
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		return "usuario/resumenorden";
	}
	
	//METODO PARA GUARDAR LA ORDEN
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//OBTIENE EL USUARIO DE LA SESION
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//guardar_Detalles
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		//Limpiar_lista_y_orden
		orden = new Orden();
		detalles.clear();
			
		return "redirect:/";
	}
	
	//METODO PARA BUSCAR UN PRODUCTO
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model ) {
		//LOGGER.info("Nombre del producto: {} ", nombre);
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		
		return "usuario/home";
	}
	
	

}

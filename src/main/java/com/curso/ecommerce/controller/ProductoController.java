package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	//LOGGER = IMPRESIONES DE PRUEBA, Y DETECTA POSIBLES ERRORES Y UBICACION DE ELLOS
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
	
	@Autowired
	private IUsuarioService usuarioService; 
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show"; 
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto,@RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		//IMPLEMENTACION DE LOGGER
		LOGGER.info("Este es el objeto producto: {}", producto);
		//OBTIENE EL USUARIO DE LA SESION
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		producto.setUsuario(u);
		
		//SUBIR LA IMAGEN
		if(producto.getId() == null) { //CUANDO SE CREA UN PRODUCTO
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {
			
		}
		//FIN
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		
		//LOGGER.info("Producto encontrado : {}", producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		
		if (file.isEmpty()) { //CUANDO EDITAMOS EL PRODUCTO PERO NO LA IMAGEN
			
			producto.setImagen(p.getImagen());
		}else {// CUANDO SE EDITA TAMBIEN LA IMAGEN
			
			if (!p.getImagen().equals("default.jpg")) { //ELIMINAR CUANDO NO SEA LA IMAGEN POR DEFECTO
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
			
		}
		//FIN 
		
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		// ELIMINAR LA IMAGEN CON EL PRODUCTO
		Producto p = new Producto();
		p= productoService.get(id).get();
		
		if (!p.getImagen().equals("default.jpg")) { //ELIMINAR CUANDO SEA LA IMAGEN POR DEFECTO
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
	
}

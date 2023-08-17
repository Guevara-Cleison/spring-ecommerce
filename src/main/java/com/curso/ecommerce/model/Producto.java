package com.curso.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String nombre;
	
	private String descipcion;
	
	private String imagen;
	
	private double precio;
	
	private int cantidad;
	
	//RELACION DE MUCHOS PRODUCTOS A UN USUARIO
	@ManyToOne()
	private Usuario usuario;

	public Producto() {
	}
	

	public Producto(int id, String nombre, String descipcion, String imagen, double precio, int cantidad,
			Usuario usuario) {
		this.id = id;
		this.nombre = nombre;
		this.descipcion = descipcion;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = cantidad;
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descipcion=" + descipcion + ", imagen=" + imagen
				+ ", precio=" + precio + ", cantidad=" + cantidad + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescipcion() {
		return descipcion;
	}

	public void setDescipcion(String descipcion) {
		this.descipcion = descipcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	//-------------->
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}

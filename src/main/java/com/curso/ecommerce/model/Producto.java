package com.curso.ecommerce.model;

public class Producto {

	private int id;

	private String nombre;
	
	private String descipcion;
	
	private String imagen;
	
	private double precio;
	
	private int cantidad;

	public Producto() {
	}

	public Producto(int id, String nombre, String descipcion, String imagen, double precio, int cantidad) {
		this.id = id;
		this.nombre = nombre;
		this.descipcion = descipcion;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = cantidad;
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

}

package arso.restaurantes.modelo;

public class Plato {

	private String nombre;
	private String descripcion;
	private double precio;
	private boolean disponible = true;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public boolean getDisponible(){
		return disponible;
	}
	public void setDisponible(boolean dis){
		this.disponible = dis;
	}	
}

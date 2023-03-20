package restaurantes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SitioTuristico {
	private String nombre;
	private String descripcion; // de GeoNames
	private String resumen; // de DBpedia
	private String imagen;

	public SitioTuristico() {

	}

	public SitioTuristico(String nombre, String descripcion, String resumen, String imagen) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.resumen = resumen;
		this.imagen = imagen;
	}

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

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}

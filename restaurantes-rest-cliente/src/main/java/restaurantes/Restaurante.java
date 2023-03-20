package restaurantes;

import java.util.List;
import java.util.Set;

public class Restaurante {
	private String id;
	private String nombre;
	private double latitud, longitud;
	private List<SitioTuristico> sitiosTuristicos;
	private Set<Plato> platos;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public List<SitioTuristico> getSitiosTuristicos() {
		return sitiosTuristicos;
	}
	public void setSitiosTuristicos(List<SitioTuristico> sitiosTuristicos) {
		this.sitiosTuristicos = sitiosTuristicos;
	}

	public Set<Plato> getPlatos() {
		return platos;
	}
	public void setPlatos(Set<Plato> platos) {
		this.platos = platos;
	}
}	

package arso.restaurantes.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import arso.especificacion.Specificable;
import arso.especificacion.Specification;
import arso.restaurantes.repositorio.Identificable;

public class Restaurante implements Identificable, Specificable<Restaurante>{
	
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)	
	private String id;
	private String nombre;
	private double latitud, longitud;
	
	private List<SitioTuristico> sitiosTuristicos;
	private Set<Plato> platos;
	
	public Restaurante() {
		sitiosTuristicos = new ArrayList<>();
		platos = new HashSet<>();
	}
	
	public Restaurante(String nombre, double latitud, double longitud) {
		this();
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
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
	public void addSitioTuristico(SitioTuristico sitioTuristico) {
		sitiosTuristicos.add(sitioTuristico);
	}
	public Set<Plato> getPlatos() {
		return platos;
	}
	public void setPlatos(Set<Plato> platos) {
		this.platos = platos;
	}
	public boolean addPlato(Plato plato) {
		// Comprobar que no haya un plato ya con ese nombre
		if(platos.stream().filter(p -> p.getNombre().equals(plato.getNombre())).count() > 0)
			return false;
		
		return platos.add(plato);
	}
	public boolean removePlato(Plato plato) {
		return platos.remove(plato);
	}
	public boolean removePlato(String nombre) {
		return platos.removeIf(p -> p.getNombre().equals(nombre));
	}
	public boolean updatePlato(Plato plato) {
		boolean result = removePlato(plato.getNombre());
		if(result)
			return addPlato(plato);
		else
			return false;
	}

	@Override
	public boolean satisfies(Specification<Restaurante> specification) {
		return specification.isSatisfied(this);
	}
}

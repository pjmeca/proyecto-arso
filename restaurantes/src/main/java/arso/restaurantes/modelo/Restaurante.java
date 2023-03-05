package arso.restaurantes.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import arso.especificacion.Specificable;
import arso.especificacion.Specification;
import arso.restaurantes.repositorio.Identificable;

public class Restaurante implements Identificable, Specificable<Restaurante>, Cloneable{
	
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

	@Override
	public int hashCode() {
		return Objects.hash(id, latitud, longitud, nombre, platos, sitiosTuristicos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurante other = (Restaurante) obj;
		return Objects.equals(id, other.id)
				&& Double.doubleToLongBits(latitud) == Double.doubleToLongBits(other.latitud)
				&& Double.doubleToLongBits(longitud) == Double.doubleToLongBits(other.longitud)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(platos, other.platos)
				&& Objects.equals(sitiosTuristicos, other.sitiosTuristicos);
	}
	
	@Override
	public Restaurante clone(){
		Restaurante r = new Restaurante(this.nombre,this.latitud,this.longitud);
		r.setId(this.id);
		r.setPlatos(new HashSet<>(this.platos));
		r.setSitiosTuristicos(new ArrayList<>(this.sitiosTuristicos));
		return r;
	}
}

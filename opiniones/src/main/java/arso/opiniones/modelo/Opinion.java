package arso.opiniones.modelo;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.bson.types.ObjectId;

public class Opinion implements Cloneable{

	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id; 
	
	private String nombre;
	private List<Valoracion> valoraciones;
	
	@BsonIgnore
	private int numValoraciones;
	@BsonIgnore
	private double calificacionMedia;
	
	public Opinion() {
		valoraciones = new ArrayList<>();
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

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}

	
	public int getNumValoraciones(){
		return valoraciones.size();
	}
	
	public double getCalificacionMedia(){
		double nota=0;
		for(Valoracion v : valoraciones){
			nota+=v.getCalificacion();
		}
		return nota/valoraciones.size();
	}
	
	public boolean addValoracion(Valoracion valoracion){
		valoraciones.removeIf(v -> v.getCorreo().equals(valoracion.getCorreo()));
		return valoraciones.add(valoracion);		
	}
	
	@Override
	public Opinion clone(){
		Opinion o = new Opinion();
		o.setId(id);
		o.setValoraciones(new ArrayList<>(valoraciones));
		
		return o;
	}
	
}

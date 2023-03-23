package arso.opiniones.modelo;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public class Opinion implements Cloneable{

	private String id; // nombre
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

	public void setId(String nombre) {
		this.id = nombre;
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

package arso.restaurantes.servicio;

public class RestauranteResumen {

	private String id;
	private String nombre;
	private double latitud, longitud;
	private float calificacionMedia; // necesario para DAWEB
	
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
	public float getCalificacionMedia() {
		return calificacionMedia;
	}
	public void setCalificacionMedia(float calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}
	
	@Override
	public String toString() {
		return "RestauranteResumen [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}
}

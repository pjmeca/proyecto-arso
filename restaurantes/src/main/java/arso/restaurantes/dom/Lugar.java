package arso.restaurantes.dom;

public class Lugar {
	
	private String nombre;
	private String descripcion;
	private String wikipediaUrl;
	
	public Lugar(String nombre, String descripcion, String wikipediaUrl) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.wikipediaUrl = wikipediaUrl;
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

	public String getWikipediaUrl() {
		return wikipediaUrl;
	}

	public void setWikipediaUrl(String wikipediaUrl) {
		this.wikipediaUrl = wikipediaUrl;
	}

	
}

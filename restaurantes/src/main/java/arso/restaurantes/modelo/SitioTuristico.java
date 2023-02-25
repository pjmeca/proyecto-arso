package arso.restaurantes.modelo;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

public class SitioTuristico {

	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)	
	private String id;
	
	private String nombre;
	private String descripcion; // de GeoNames
	private String resumen; // de DBpedia
	private String wikipediaUrl;
	private String jsonUrl;
	private List<String> categorias;
	private List<String> linksExternos;
	private String imagen;
	
	public SitioTuristico() {
		categorias = new ArrayList<>();
		linksExternos = new ArrayList<>();
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

	public String getJsonUrl() {
		return jsonUrl;
	}

	public void setJsonUrl(String jsonUrl) {
		this.jsonUrl = jsonUrl;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}
	
	public void addCategoria(String categoria) {
		categorias.add(categoria);
	}
	
	public List<String> getLinksExternos() {
		return linksExternos;
	}

	public void setLinksExternos(List<String> links) {
		this.linksExternos=links;
	}
	
	public void addLinkExterno(String link) {
		linksExternos.add(link);
	}
	
	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
}
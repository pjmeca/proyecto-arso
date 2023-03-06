package arso.restaurantes.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SitioTuristico {
	
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

	@Override
	public int hashCode() {
		return Objects.hash(categorias, descripcion, imagen, jsonUrl, linksExternos, nombre, resumen, wikipediaUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SitioTuristico other = (SitioTuristico) obj;
		return Objects.equals(categorias, other.categorias) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(imagen, other.imagen) && Objects.equals(jsonUrl, other.jsonUrl)
				&& Objects.equals(linksExternos, other.linksExternos) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(resumen, other.resumen) && Objects.equals(wikipediaUrl, other.wikipediaUrl);
	}
	
}

package restaurantes.rest;

import java.util.List;

public class ListadoSitioTuristico{
	
	public class SitioTuristicoResumen {
		private String nombre;
		private String descripcion; // de GeoNames
		private String resumen; // de DBpedia
		private String imagen;
		
		public SitioTuristicoResumen(String nombre, String descripcion, String resumen, String imagen) {
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

	private List<SitioTuristicoResumen> lista;
	
	public List<SitioTuristicoResumen> getLista() {
		return lista;
	}
	
	public void setLista(List<SitioTuristicoResumen> lista) {
		this.lista = lista;
		
		System.out.println(this.lista.size());
	}
}

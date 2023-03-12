package restaurantes.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import arso.restaurantes.modelo.SitioTuristico;

@XmlRootElement
public class ListadoSitioTuristico{

	@XmlElementWrapper(name="sitiosTuristicos")
	private List<SitioTuristico> lista;
	
	public List<SitioTuristico> get() {
		return lista;
	}
	
	public void set(List<SitioTuristico> lista) {
		this.lista = lista;
		System.out.println(this.lista.size());
	}
}

package restaurantes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListadoSitioTuristico {
	private List<SitioTuristico> lista;

	public ListadoSitioTuristico() {

	}

	public List<SitioTuristico> getLista() {
		return lista == null ? new ArrayList<SitioTuristico>() : lista;
	}

	public void setLista(List<SitioTuristico> lista) {
		this.lista = lista;
	}
	
	
}

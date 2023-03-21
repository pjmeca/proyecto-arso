package restaurantes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListadoRestaurantes {
	
	public static class RestauranteResumen {
		private String url;
		private Restaurante resumen;
		
		public RestauranteResumen() {
			
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Restaurante getResumen() {
			return resumen;
		}

		public void setResumen(Restaurante restaurante) {
			this.resumen = restaurante;
		}
	}

	private List<RestauranteResumen> restaurantes;
	
	public ListadoRestaurantes() {
		
	}

	public List<RestauranteResumen> getRestaurantes() {
		return restaurantes == null ? new ArrayList<RestauranteResumen>() : restaurantes;
	}

	public void setRestaurantes(List<RestauranteResumen> lista) {
		this.restaurantes = lista;
	}	
	
}

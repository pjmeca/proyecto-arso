package arso.restaurantes.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import arso.restaurantes.dom.BuscadorCP;
import arso.restaurantes.dom.Lugar;

public class WikipediaExtractor {

	public static WikipediaExtractor wpExtractor;

	private WikipediaExtractor() {
	}

	public static WikipediaExtractor getInstance() {
		if (wpExtractor != null)
			return wpExtractor;

		wpExtractor = new WikipediaExtractor();
		return wpExtractor;
	}

	private JsonArray getPropiedad(String ultimo, String key) throws IOException {
		String jsonUrl = "https://es.dbpedia.org/data/" + ultimo + ".json";
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(new URL(jsonUrl).openStream(), StandardCharsets.UTF_8));

		JsonReader jsonReader = Json.createReader(in);
		JsonObject obj = jsonReader.readObject();
		JsonObject obj2 = obj.getJsonObject(URLDecoder.decode("http://es.dbpedia.org/resource/" + ultimo));
		return obj2.getJsonArray(key);
	}

	public List<Lugar> getInfo(String codigo) {
		List<Lugar> lugares = BuscadorCP.getInstance().findByCP(codigo);

		for (Lugar lugar : lugares) {

			// Generar URL DBpedia
			String[] trozos = lugar.getWikipediaUrl().split("/");
			String ultimo = trozos[trozos.length - 1];
			String jsonUrl = "https://es.dbpedia.org/data/" + ultimo + ".json";
			System.out.println("URL DBpedia generada: " + jsonUrl);
			lugar.setJsonUrl(jsonUrl);

			// Extraer propiedades
			try {
				
				// Resumen
				JsonArray aux = getPropiedad(ultimo, "http://dbpedia.org/ontology/abstract");
				if(aux != null) {
					lugar.setResumen(aux.getJsonObject(0).getString("value"));
				}
				System.out.println("Resumen: " + lugar.getResumen());
				
				// Categorías
				JsonArray categoriasJson = getPropiedad(ultimo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
				if(categoriasJson != null)
					for(int i=0; i<categoriasJson.size(); i++) {
						lugar.addCategoria(categoriasJson.getJsonObject(i).getString("value"));
					}
				System.out.println("Categorías: " + lugar.getCategorias());
			
				//Enlaces externos
				JsonArray linksJson = getPropiedad(ultimo,"http://dbpedia.org/ontology/wikiPageExternalLink");
				if(linksJson != null)
					for(int i=0; i<linksJson.size(); i++) {
						lugar.addLinkExterno(linksJson.getJsonObject(i).getString("value"));
					}
				System.out.println("Enlaces externos: " + lugar.getLinksExternos());
				
				//Imagen wikipedia
				aux = getPropiedad(ultimo,"http://es.dbpedia.org/property/imagen");
				if(aux != null)
					lugar.setImagen(aux.getJsonObject(0).getString("value"));
				System.out.println("Imagen: " + lugar.getImagen());
				
				System.out.println();
			} catch (IOException e) {
				System.err.println("No se ha podido leer de DBpedia.");
			}

		}
		
		return lugares;
	}
}

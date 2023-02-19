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

	public void getInfo(String codigo) {
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
				lugar.setResumen(
						getPropiedad(ultimo, "http://dbpedia.org/ontology/abstract")
						.getJsonObject(0).getString("value"));
				System.out.println("Resumen: " + lugar.getResumen());
			} catch (IOException e) {
				System.err.println("No se ha podido leer de DBpedia.");
			}

		}
	}
}

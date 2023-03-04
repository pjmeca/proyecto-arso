package arso.restaurantes.dom;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import arso.restaurantes.modelo.SitioTuristico;

public final class BuscadorGeoNames {

	public static BuscadorGeoNames buscador;

	public static final int RADIO_DEFAULT = 20;
	private static final int REINTENTOS_CONEXION = 10;
	private static final String username = /*"arso"*/ "arsojp";

	private BuscadorGeoNames() {
	}

	public static BuscadorGeoNames getInstance() {
		if (buscador != null)
			return buscador;

		buscador = new BuscadorGeoNames();
		return buscador;
	}

	public static void printDocument(Document doc, OutputStream out) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;

			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
		} catch (TransformerException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<SitioTuristico> find(String url) {

		ArrayList<SitioTuristico> lugares = new ArrayList<>();
		
		try {
			// 1. Obtener una factoría
			DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

			// 2. Pedir a la factoría la construcción del analizador
			DocumentBuilder analizador;
			analizador = factoria.newDocumentBuilder();

			System.out.println("Buscando en GeoNames...");

			// 3. Analizar el documento
			int reintentos = REINTENTOS_CONEXION;
			Document documento = null;
			while (reintentos > 0) {
				try {
					 documento = analizador.parse(url);
					 reintentos = 0;
				} catch (java.net.ConnectException e) {
					System.err.println("Fallo de conexión, reintentando...");
					reintentos--;
				}
			}
			if(documento == null) {
				System.err.println("Agotados los reintentos de conexión");
				throw new java.net.ConnectException();
			}
				

			// printDocument(documento, System.out);

			NodeList elementos = documento.getElementsByTagName("entry");
			System.out.println("Búsqueda completada. " + elementos.getLength() + " resultados");

			for (int i = 0; i < elementos.getLength(); i++) {
				Element entrada = (Element) elementos.item(i);
				Node nombre = entrada.getElementsByTagName("title").item(0);
				String nombreText = nombre == null ? "" : nombre.getTextContent();
				Node descripcion = entrada.getElementsByTagName("summary").item(0);
				String descripcionText = descripcion == null ? "" : descripcion.getTextContent();
				Node wikipediaUrl = entrada.getElementsByTagName("wikipediaUrl").item(0);
				String wikipediaUrlText = wikipediaUrl == null ? "" : wikipediaUrl.getTextContent();

				// System.out.println(nombreText + "\n" + descripcionText + "\n" +
				// wikipediaUrlText);
				SitioTuristico l = new SitioTuristico();
				l.setNombre(nombreText);
				l.setDescripcion(descripcionText);
				l.setWikipediaUrl(wikipediaUrlText);
				lugares.add(l);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lugares;
	}
	
	public List<SitioTuristico> findByCP(String codigo) {

		// Generar la URL de la consulta
		String url = "http://api.geonames.org/findNearbyWikipedia?postalcode=" + codigo + "&country=ES&radius="
				+ RADIO_DEFAULT + "&username=" + username + "&lang=es";
		System.out.println("URL GeoNames generada: " + url);
		
		return find(url);
	}
	
	public List<SitioTuristico> findByCoord(double latitud, double longitud) {

		// Generar la URL de la consulta
		String url = "http://api.geonames.org/findNearbyWikipedia?lat=" + latitud + "&lng=" + longitud + "&country=ES&radius="
				+ RADIO_DEFAULT + "&username=" + username + "&lang=es";
		System.out.println("URL GeoNames generada: " + url);
		
		return find(url);
	}
}

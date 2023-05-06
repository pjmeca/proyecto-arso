package arso.servicio;

import java.util.HashMap;
import java.util.Map;

import arso.opiniones.servicio.EventoServicio;
import arso.restaurantes.servicio.IServicioRestaurantes;
import arso.utils.PropertiesReader;

/*
 * Factoría que encapsula la implementación de un servicio.
 * 
 * Utiliza un fichero de propiedades para cargar la implementación.
 */

public class FactoriaServicios {

	private static final String PROPERTIES = "servicios.properties";

	private static Map<Class<?>, Object> servicios = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T getServicio(Class<T> servicio) {

		try {
			if (servicios.containsKey(servicio)) {
				return (T) servicios.get(servicio);
			} else {
				PropertiesReader properties = new PropertiesReader(PROPERTIES);
				String clase = properties.getProperty(servicio.getName());

				T servicioObject = (T) Class.forName(clase).getConstructor().newInstance();

				try {
					if (servicioObject instanceof IServicioRestaurantes)
						EventoServicio.suscribirse((IServicioRestaurantes) servicioObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				servicios.put(servicio, servicioObject);

				return servicioObject;
			}

		} catch (Exception e) {

			throw new RuntimeException("No se ha podido obtener la implementacion del servicio: " + servicio.getName());
		}

	}

}

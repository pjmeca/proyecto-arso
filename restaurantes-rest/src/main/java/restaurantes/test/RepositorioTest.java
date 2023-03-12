package restaurantes.test;

import arso.repositorio.RepositorioException;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;
import arso.restaurantes.repositorio.RepositorioRestaurantesMemoria;

/*
 * En la carpeta de código fuente se crea el fichero "repositorios.properties" para 
 * reemplazar el valor por defecto (configurado en el proyecto Bookle) y establecer el repositorio de pruebas
 */

public class RepositorioTest extends RepositorioRestaurantesMemoria {

	public RepositorioTest() {		
		
		 // Restaurante 1
        
   		Restaurante restaurante1 = new Restaurante();
		restaurante1.setNombre("Restaurante1");
		restaurante1.setLatitud(37.983954);
		restaurante1.setLongitud(-1.129325);
		Plato platoR1 = new Plato();
		platoR1.setNombre("Plato R1");
		platoR1.setDescripcion("Plato");
		platoR1.setPrecio(0);
		restaurante1.addPlato(platoR1);
		SitioTuristico sitio = new SitioTuristico();
		sitio.setNombre("Sitio");
		sitio.setDescripcion("Descripcion");
		sitio.setResumen("Resumen");
		restaurante1.addSitioTuristico(sitio);
		
		// Actividad 2
        
   		Restaurante restaurante2 = new Restaurante();
		restaurante2.setNombre("Restaurante2");
		restaurante2.setLatitud(37.980260);
		restaurante2.setLongitud(-1.129792);
		Plato platoR21 = new Plato();
		platoR21.setNombre("Plato R21");
		platoR21.setDescripcion("Muy rico");
		platoR21.setPrecio(2);
		restaurante2.addPlato(platoR21);
		Plato platoR22 = new Plato();
		platoR22.setNombre("Otro plato del 2");
		platoR22.setDescripcion("Muy malo");
		platoR22.setPrecio(3);
		restaurante2.addPlato(platoR22);
		SitioTuristico sitio2 = new SitioTuristico();
		sitio.setNombre("Plaza");
		sitio.setDescripcion("Descri");
		sitio.setResumen("Melon");
		restaurante2.addSitioTuristico(sitio2);
		
		try {
			add(restaurante1);
			add(restaurante2);
		} catch (RepositorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}

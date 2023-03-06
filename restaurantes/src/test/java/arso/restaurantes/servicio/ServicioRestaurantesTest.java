package arso.restaurantes.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.FactoriaRepositorios;
import arso.repositorio.RepositorioException;
import arso.restaurantes.especificacion.IsContienePlatoSpecification;
import arso.restaurantes.especificacion.IsRadioSpecification;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;

public class ServicioRestaurantesTest {

	private ServicioRestaurantes servicio;
	private Restaurante r1Obj;
	private String r1, r2, r3, r4;

	@BeforeEach
	public void init() {		
		servicio = new ServicioRestaurantes();
		try {
			r1Obj = new Restaurante("Burger", 1000, 1000);
			r1 = servicio.create(r1Obj);
			r2 = servicio.create(new Restaurante("Comic", 1100, 1100));
			r3 = servicio.create(new Restaurante("McDonalds", 900, 900));
			r4 = servicio.create(new Restaurante("La Catedral", 37.983976, -1.128910));
		} catch (RepositorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@AfterEach
	public void end() {
		assertDoesNotThrow(() -> servicio.removeAll());
	}

	@Test
	public void createTest() {
		// Errores
		assertThrows(RepositorioException.class, () -> {
			servicio.create(null);
		});
		assertThrows(RepositorioException.class, () -> {
			servicio.create(new Restaurante(null, 0, 0));
		});
		assertThrows(RepositorioException.class, () -> {
			servicio.create(new Restaurante("", 0, 0));
		});
		assertThrows(RepositorioException.class, () -> {
			servicio.create(new Restaurante(" ", 0, 0));
		});
		assertThrows(RepositorioException.class, () -> {
			Restaurante res = new Restaurante("prueba", 0, 0);
			Restaurante res2 = servicio.getRestaurante(r1);
			res.setId(res2.getId());
			servicio.create(res);
		});

		// Debería funcionar
		String id = "";
		try {
			id = servicio.create(new Restaurante("Plaza", 10, 1000));
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
		assertTrue(!id.isBlank());
		try {
			servicio.removeRestaurante(id);
		} catch (RepositorioException | EntidadNoEncontradaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateTest() {
		assertThrows(RepositorioException.class, () -> {
			servicio.update(null);
		});

		assertThrows(RepositorioException.class, () -> {
			servicio.update(new Restaurante());
		});
		assertThrows(EntidadNoEncontradaException.class, () -> {
			Restaurante r = new Restaurante("Pizza", 100, 100);
			r.setId("1");
			servicio.update(r);
		});

		try {
			Restaurante r = servicio.getRestaurante(r1);
			double lat = r.getLatitud();
			double lon = r.getLongitud();
			r.setLatitud(lat + 1);
			r.setLongitud(lon + 1);
			servicio.update(r);
			r = servicio.getRestaurante(r1);
			assertTrue(r.getLatitud() == (lat + 1) && r.getLongitud() == (lon + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getRestauranteTest() {
		// Debería funcionar
		assertDoesNotThrow(() -> assertTrue(r1Obj.equals(servicio.getRestaurante(r1))));

		// Errores
		assertThrows(RepositorioException.class, () -> servicio.getRestaurante(null));
		assertThrows(RepositorioException.class, () -> servicio.getRestaurante(""));
		assertThrows(RepositorioException.class, () -> servicio.getRestaurante(" "));
	}
	
	@Test
	public void getSitiosTuristicosProximosTest() {	
		assertDoesNotThrow(() -> assertTrue(servicio.getSitiosTuristicosProximos(r4).size() > 0));
	}

	@Test
	public void setSitiosTuristicosDestacadosTest() {
		assertThrows(RepositorioException.class, () -> {
			servicio.setSitiosTuristicosDestacados(r1, null);
		});
		ArrayList<SitioTuristico> lista = new ArrayList<SitioTuristico>();
		SitioTuristico st = new SitioTuristico();
		st.setNombre("Monumento");
		st.setDescripcion("Sitio de prueba");
		lista.add(st);
		
		assertDoesNotThrow(() -> servicio.setSitiosTuristicosDestacados(r4, lista));
		assertDoesNotThrow(() -> {
			Restaurante r = servicio.getRestaurante(r4);
			assertTrue(r.getSitiosTuristicos().contains(lista.get(0)));
			servicio.setSitiosTuristicosDestacados(r4, new ArrayList<SitioTuristico>());
			r = servicio.getRestaurante(r4);
			assertTrue(r.getSitiosTuristicos().isEmpty());
		});
	}
	
	@Test
	public void addPlatoTest() {
		Plato p = new Plato();
		p.setNombre("Macarrones");
		p.setDescripcion("Macarrones deliciosos");
		p.setPrecio(10);
		
		assertThrows(RepositorioException.class, () -> servicio.addPlato(r1, null));
		assertThrows(RepositorioException.class, () -> servicio.addPlato(null, p));
		assertDoesNotThrow(() -> servicio.addPlato(r1, p));
		assertDoesNotThrow(() -> assertTrue(servicio.getRestaurante(r1).getPlatos().stream().filter(plato -> plato.getNombre().equals(p.getNombre())).findFirst().get().getPrecio() == 10));
		assertThrows(RepositorioException.class, () -> servicio.addPlato(r1, p));
	}

	@Test
	public void removePlatoTest() {	
		assertThrows(RepositorioException.class, () -> servicio.removePlato(r3,null));
		assertThrows(RepositorioException.class, () -> servicio.removePlato(r3,""));
		assertThrows(RepositorioException.class, () -> servicio.removePlato(r3,"Plati inexistente"));
		assertDoesNotThrow(() -> {
			Plato p = new Plato();
			p.setNombre("Huevo");
			p.setDescripcion("Tremendo huevo frito");
			p.setPrecio(5);	
			servicio.addPlato(r3,p);
			servicio.removePlato(r3,"Huevo");
			assertTrue(!servicio.getRestaurante(r3).getPlatos().contains(p));
		});
	}
	
	@Test
	public void updatePlatoTest() {
		// Debería fallar		
		assertThrows(RepositorioException.class, () -> servicio.updatePlato(r1, null));
		Plato pMal = new Plato();
		pMal.setNombre("prueba");
		pMal.setDescripcion("");
		pMal.setPrecio(10);
		assertThrows(RepositorioException.class, () -> servicio.updatePlato(r1, pMal));
		
		// No debería fallar
		Plato p = new Plato();
		p.setNombre("Pasta Carbonara");
		p.setDescripcion("Pasta carbonara muy sabrosa");
		p.setPrecio(15.0);
		assertDoesNotThrow(() -> servicio.addPlato(r1, p));
		p.setPrecio(10.0);
		assertDoesNotThrow(() -> servicio.updatePlato(r1, p));
		assertDoesNotThrow(() -> assertTrue(servicio.getRestaurante(r1).getPlatos().stream().filter(plato -> plato.getNombre().equals(p.getNombre())).findFirst().get().getPrecio() == 10.0));
	}
	
	@Test
	public void removeRestauranteTest() {	
		//Si el id no existe, es nulo o esta vacio ya se comprueba en la llamada a getRestaurante
		assertDoesNotThrow(() -> servicio.removeRestaurante(r2));
		assertThrows(EntidadNoEncontradaException.class, () -> servicio.removeRestaurante(r2));
	}
	
	@Test
	public void getListadoRestaurantesTest() {
		assertDoesNotThrow(() -> assertTrue(servicio.getListadoRestaurantes().size() == 4));
	}
	
	@Test
	public void getListadoRestaurantesByEspecificacionTest(){
		
		assertThrows(RepositorioException.class, () -> servicio.getListadoRestaurantesBySpecification(null));
		
		//Contiene plato
		Plato p = new Plato();
		p.setNombre("Cafe");
		p.setDescripcion("Cafe extremadamente estrafalario");
		p.setPrecio(50);
		assertDoesNotThrow(() -> servicio.addPlato(r4, p));
		
		
		assertDoesNotThrow(() -> {
			List<RestauranteResumen> l ;
			l = servicio.getListadoRestaurantesBySpecification(new IsContienePlatoSpecification("Plato inexistente"));
			assertTrue(l.size()==0);
		});
		assertDoesNotThrow(() -> {
			List<RestauranteResumen> l ;
			l = servicio.getListadoRestaurantesBySpecification(new IsContienePlatoSpecification("Cafe"));
			assertTrue(l.size()==1 && l.get(0).getId().equals(r4));
			servicio.addPlato(r3,p);
			l = servicio.getListadoRestaurantesBySpecification(new IsContienePlatoSpecification("Cafe"));
			assertTrue(l.size()==2);
		});
		
		//Radio
		assertDoesNotThrow(() -> {
			List<RestauranteResumen> l ;
			l = servicio.getListadoRestaurantesBySpecification(new IsRadioSpecification(2000, 2000, 100));//No hay restaurantes
			assertTrue(l.size()==0);
			l = servicio.getListadoRestaurantesBySpecification(new IsRadioSpecification(1000, 1000, 110));//Estan el burger,comic y mcdonalds
			assertTrue(l.size()==3);
			l = servicio.getListadoRestaurantesBySpecification(new IsRadioSpecification(1000, 1000, 0));//El burger esta en ese mismo punto
			assertTrue(l.size()==1 && l.get(0).getId().equals(r1));
		});
	}
}

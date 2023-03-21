package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import restaurantes.ListadoRestaurantes;
import restaurantes.ListadoSitioTuristico;
import restaurantes.Plato;
import restaurantes.Restaurante;
import restaurantes.RestaurantesRetrofit;
import restaurantes.SitioTuristico;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class RestaurantesRestTest {

	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/api/")
			.addConverterFactory(JacksonConverterFactory.create()).build();

	RestaurantesRetrofit service = retrofit.create(RestaurantesRetrofit.class);

	@Test
	void getRestauranteTest() throws IOException {
		// Petición correcta
		Response<Restaurante> correcta = service.getRestaurante("1").execute();
		assertTrue(correcta.code() == 200);
		Restaurante r = correcta.body();
		assertTrue(r.getId().equals("1") && r.getNombre().equals("Restaurante1"));
		assertTrue(r.getPlatos().stream().filter(p -> p.getNombre().equals("Plato R1")).count() == 1);
		assertFalse(r.getPlatos().stream().filter(p -> p.getNombre().equals("Plato R21")).count() == 1);
		assertTrue(r.getSitiosTuristicos().stream().filter(s -> s.getNombre().equals("Sitio")).count() == 1);

		// Petición incorrecta
		Response<Restaurante> incorrecta = service.getRestaurante("-1").execute();
		assertTrue(incorrecta.code() == 404);
	}

	@Test
	void createTest() throws IOException {
		Restaurante r = new Restaurante();
		String nombre = RandomStringUtils.randomAlphanumeric(20);
		r.setNombre(nombre);
		r.setLatitud(0);
		r.setLongitud(0);
		r.setPlatos(new HashSet<Plato>());
		r.setSitiosTuristicos(new ArrayList<SitioTuristico>());	
		Response<Void> correcta = service.create(r).execute();
		assertTrue(correcta.code() == 201);
		assertTrue(correcta.headers().get("Location") != null);
		String[] url = correcta.headers().get("Location").split("/");
		String id = url[url.length - 1];
		assertDoesNotThrow(() -> Integer.parseInt(id));
	}
	
	@Test
	void updateTest() throws IOException {		
		Restaurante r = service.getRestaurante("2").execute().body();	
		r.setId(null);
		r.setNombre("Restaurante actualizado");
		
		// Falla porque no tiene id
		Response<Void> sinid = service.update("2", r).execute();
		assertTrue(sinid.code() == 400);
		
		// Correcto
		r.setId("2");
		Response<Void> correcta = service.update("2", r).execute();
		assertTrue(correcta.code() == 204);
		
		// Falla porque no existe
		r.setId("-1");
		Response<Void> incorrecta = service.update("-1", r).execute();
		assertTrue(incorrecta.code() == 404);
	}
	
	@Test
	void removeRestauranteTest() throws IOException {
		// Falla porque no existe
		Response<Void> incorrecta = service.removeRestaurante("-1").execute();
		assertTrue(incorrecta.code() == 404);
		
		// Correcta
		assertTrue(service.getRestaurante("3").execute().code() == 200); // ejecutar el create primero
		Response<Void> correcta = service.removeRestaurante("3").execute(); 
		assertTrue(correcta.code() == 204);
	}
	
	@Test
	void getSitiosProximosRestauranteTest() throws IOException {
		// Falla porque el restaurante no existe
		Response<ListadoSitioTuristico> incorrecta = service.getSitiosProximosRestaurante("-1").execute();
		assertTrue(incorrecta.code() == 404);
		
		// Correcta
		Response<ListadoSitioTuristico> correcta = service.getSitiosProximosRestaurante("1").execute();
		assertTrue(correcta.code() == 200);
		assertTrue(correcta.body() != null);
		assertTrue(!correcta.body().getLista().isEmpty());
	}

	@Test
	void getListadoRestaurantesTest() throws IOException {
		// Buscar todos
		Response<ListadoRestaurantes> res = service.getListadoRestaurantes(-1, 0., 0,"").execute();
		assertTrue(res.body().getRestaurantes().size()>=2);
		// Nombre plato
		Response<ListadoRestaurantes> res2 = service.getListadoRestaurantes(-1, 0, 0,"Plato R1").execute();
		assertTrue(res2.body().getRestaurantes().size()==1);
		//Radio busqueda
		Response<ListadoRestaurantes> res3 = service.getListadoRestaurantes(10, 102.0, 102.0,"").execute();
		assertTrue(res3.body().getRestaurantes().size()==1);
		//Ambos
		Response<ListadoRestaurantes> res4 = service.getListadoRestaurantes(10, 102, 102,"Plato R21").execute();
		assertTrue(res4.body().getRestaurantes().size()==1);
		//Ninguno
		Response<ListadoRestaurantes> res5 = service.getListadoRestaurantes(200, 0, 0,"Noexiste").execute();
		assertTrue(res5.body().getRestaurantes().size()==0);
	}

	@Test
	void PlatoTest() throws IOException {
		addPlatoTest();
		updatePlatoTest();
		removePlatoTest();
	}

	private void removePlatoTest() throws IOException {
		// No contiene plato
		Response<Void> cont3 = service.removePlato("1", "noexiste").execute();
		assertTrue(cont3.code() == 404);

		// id incorrecto
		Response<Void> incorrecto = service.removePlato("noexiste", "Pepinillos").execute();
		assertTrue(incorrecto.code() == 404);
		Response<Restaurante> r1 = service.getRestaurante("1").execute();
		Restaurante restaurante1 = r1.body();
		assertTrue(restaurante1.getPlatos().stream().filter(p -> p.getNombre().equals("Pepinillos")).count() == 1);

		// Peticion correcta
		Response<Void> resRe = service.removePlato("1", "Pepinillos").execute();
		assertTrue(resRe.code() == 204);
		Response<Restaurante> rRe = service.getRestaurante("1").execute();
		Restaurante restauranteRe = rRe.body();
		assertTrue(restauranteRe.getPlatos().stream().filter(p -> p.getNombre().equals("Pepinillos")).count() == 0);
	}

	private void updatePlatoTest() throws IOException {
		Plato platoUp = new Plato();
		platoUp.setNombre("Pepinillos");
		platoUp.setDescripcion("Pepinillos actualizados");// Nueva descripcion
		platoUp.setPrecio(10.0);// Nuevo precio

		// No contiene plato
		Response<Void> cont2 = service.updatePlato("2", platoUp).execute();
		assertTrue(cont2.code() == 404);

		// No id
		Response<Void> noid = service.updatePlato("noexiste", platoUp).execute();
		assertTrue(noid.code() == 404);

		// Plato nulo
		Response<Void> nulo = service.updatePlato("2", platoUp).execute();
		assertTrue(nulo.code() == 404);

		// Peticion correcta
		Response<Void> resUp = service.updatePlato("1", platoUp).execute();
		assertTrue(resUp.code() == 204);
		Response<Restaurante> rUp = service.getRestaurante("1").execute();
		Restaurante restauranteUp = rUp.body();
		Plato pl = null;
		for (Plato p : restauranteUp.getPlatos()) {
			if (p.getNombre().equals(platoUp.getNombre()))
				pl = p;
		}

		assertTrue(pl.getDescripcion().equals(platoUp.getDescripcion()) && platoUp.getPrecio() == pl.getPrecio());
	}

	private void addPlatoTest() throws IOException {
		//ADD
		Plato plato = new Plato();
		plato.setNombre("Pepinillos");
		plato.setDescripcion("Pepinillos picantes");
		plato.setPrecio(3.5);

		// Id restaurante vacio
		Response<Void> vac = service.addPlato("", plato).execute();
		assertTrue(vac.code() == 404);

		// Peticion correcta
		Response<Void> resAd = service.addPlato("1", plato).execute();
		assertTrue(resAd.code() == 204);
		Response<Restaurante> rAd = service.getRestaurante("1").execute();
		Restaurante restauranteAd = rAd.body();
		assertTrue(restauranteAd.getPlatos().stream().filter(p -> p.getNombre().equals(plato.getNombre())).count() == 1);

		// Plato ya contenido
		Response<Void> cont = service.addPlato("1", plato).execute();
		assertTrue(cont.code() == 400); 
	}

}

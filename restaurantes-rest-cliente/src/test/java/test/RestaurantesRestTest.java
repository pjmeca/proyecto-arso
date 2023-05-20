package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import restaurantes.ListadoRestaurantes;
import restaurantes.ListadoSitioTuristico;
import restaurantes.Plato;
import restaurantes.Restaurante;
import restaurantes.RestaurantesRetrofit;
import restaurantes.SitioTuristico;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*
 * Importante: cambiar al repositorio en memoria para poder hacer las pruebas con ID no aleatorios
 */
class RestaurantesRestTest {

	RestaurantesRetrofit service = createService();

	private static final String BASE_URL = "http://localhost:8090/";
	private static final String AUTH_HEADER = "Authorization";
	private static final String JWT_TOKEN = System.getenv("JWT");

	public static RestaurantesRetrofit createService() {
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(new Interceptor() {
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException {
				Request originalRequest = chain.request();
				Request.Builder requestBuilder = originalRequest.newBuilder().header(AUTH_HEADER,
						"Bearer " + JWT_TOKEN);

				Request modifiedRequest = requestBuilder.build();
				return chain.proceed(modifiedRequest);
			}
		});

		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
				.addConverterFactory(JacksonConverterFactory.create()).client(httpClient.build()).build();

		return retrofit.create(RestaurantesRetrofit.class);
	}

	private Restaurante restaurante1, restaurante2;

	@BeforeEach
	void init() throws IOException {

		Response<ListadoRestaurantes> res = service.getListadoRestaurantes(-1, 100, 100, "").execute();

		if (res.body().getRestaurantes().size() > 0) {
			return;
		}

		// Restaurante 1
		restaurante1 = new Restaurante();
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

		// Restaurante 2
		restaurante2 = new Restaurante();
		restaurante2.setNombre("Restaurante2");
		restaurante2.setLatitud(100.0);
		restaurante2.setLongitud(100.0);
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
		sitio2.setNombre("Plaza");
		sitio2.setDescripcion("Descri");
		sitio2.setResumen("Melon");
		restaurante2.addSitioTuristico(sitio2);

		service.create(restaurante1).execute();
		service.create(restaurante2).execute();
	}

	@Test
	void getRestauranteTest() throws IOException {
		// Petición correcta
		Response<Restaurante> correcta = service.getRestaurante("1").execute();
		assertTrue(correcta.code() == 200);
		Restaurante r = correcta.body();
		assertTrue(r.getId().equals("1") && r.getNombre().equals("Restaurante1"));
		assertTrue(r.getPlatos().stream().filter(p -> p.getNombre().equals("Plato R1")).count() == 1);
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
		Response<ListadoRestaurantes> res = service.getListadoRestaurantes(-1, 0., 0, "").execute();
		assertTrue(res.body().getRestaurantes().size() >= 2);
		// Nombre plato
		Response<ListadoRestaurantes> res2 = service.getListadoRestaurantes(-1, 0, 0, "Plato R1").execute();
		assertTrue(res2.body().getRestaurantes().size() == 1);
		// Radio busqueda
		Response<ListadoRestaurantes> res3 = service.getListadoRestaurantes(10, 102.0, 102.0, "").execute();
		assertTrue(res3.body().getRestaurantes().size() == 1);
		// Ambos
		Response<ListadoRestaurantes> res4 = service.getListadoRestaurantes(10, 102, 102, "Plato R21").execute();
		assertTrue(res4.body().getRestaurantes().size() == 1);
		// Ninguno
		Response<ListadoRestaurantes> res5 = service.getListadoRestaurantes(200, 0, 0, "Noexiste").execute();
		assertTrue(res5.body().getRestaurantes().size() == 0);
	}

	@Test
	void PlatoTest() throws IOException {
		addPlatoTest();
		updatePlatoTest();
		removePlatoTest();
	}

	private void addPlatoTest() throws IOException {
		// ADD
		Plato plato = new Plato();
		plato.setNombre("Pepinillos");
		plato.setDescripcion("Pepinillos picantes");
		plato.setPrecio(3.5);

		// Id restaurante vacio
		Response<Void> vac = service.addPlato("noexiste", plato).execute();
		System.out.println(vac.code());
		assertTrue(vac.code() == 404);

		// Peticion correcta
		Response<Void> resAd = service.addPlato("1", plato).execute();
		assertTrue(resAd.code() == 204);
		Response<Restaurante> rAd = service.getRestaurante("1").execute();
		Restaurante restauranteAd = rAd.body();
		assertTrue(
				restauranteAd.getPlatos().stream().filter(p -> p.getNombre().equals(plato.getNombre())).count() == 1);

		// Plato ya contenido
		Response<Void> cont = service.addPlato("1", plato).execute();
		assertTrue(cont.code() == 400);
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

}

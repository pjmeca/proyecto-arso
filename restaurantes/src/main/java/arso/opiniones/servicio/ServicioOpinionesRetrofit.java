package arso.opiniones.servicio;

import java.io.IOException;
import java.util.List;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.RepositorioException;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class ServicioOpinionesRetrofit implements IServicioOpiniones{
	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://opiniones-rest:5041/api/")
			.addConverterFactory(JacksonConverterFactory.create())
			.build();

	OpinionesRetrofit service = retrofit.create(OpinionesRetrofit.class);

	@Override
	public String altaOpiniones(String nombre) throws RepositorioException {
		if(nombre.isBlank())
			throw new RepositorioException("El nombre del recurso no es válido.");
		
		try {
			Response<ResponseBody> res = service.create(nombre).execute();
			ResponseBody rb = res.body();
			if(rb == null)
				throw new IOException();
			return rb.string();
		} catch (IOException e) {
			throw new RepositorioException("Error al crear la opinión");
		}
	}

	@Override
	public List<Valoracion> getValoraciones(String id) throws RepositorioException {
		if(id.isBlank())
			throw new RepositorioException("El nombre del recurso no es válido.");
		
		try {
			Response<List<Valoracion>> res = service.getValoraciones(id).execute();
			return res.body();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RepositorioException("Error al recuperar las valoraciones");			
		}
	}

}

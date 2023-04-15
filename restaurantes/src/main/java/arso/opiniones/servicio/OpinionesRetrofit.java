package arso.opiniones.servicio;

import java.util.List;
import arso.opiniones.modelo.Valoracion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpinionesRetrofit {
	
	@POST("opiniones/{nombre}")
	Call<ResponseBody> create(@Path("nombre") String nombre);
		
	@GET("opiniones/{id}/valoraciones")
	Call<List<Valoracion>> getValoraciones(@Path("id") String id);
}

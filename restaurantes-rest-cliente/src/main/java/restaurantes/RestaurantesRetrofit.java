package restaurantes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantesRetrofit {
		
		@GET("restaurantes/{id}")
		Call<Restaurante> getRestaurante(@Path("id") String id);
		
		@POST("restaurantes")
		Call<Void> create(@Body Restaurante restaurante);

		@PUT("restaurantes/{id}")
		Call<Void> update(@Path("id") String id, @Body Restaurante restaurante);

		@DELETE("restaurantes/{id}")
		Call<Void> removeRestaurante(@Path("id") String id);
		
		@GET("restaurantes/{id}/sitiosProximos")
		Call<ListadoSitioTuristico> getSitiosProximosRestaurante(@Path("id") String id);
		
		@GET("restaurantes")
		Call<ListadoRestaurantes> getListadoRestaurantes(@Query("radio") double radio, @Query("latitud") double latitud, @Query("longitud") double longitud, @Query("plato") String plato); 	

		@POST("restaurantes/{id}/plato")
		Call<Void> addPlato(@Path("id") String id, @Body Plato plato); 
		
		@PUT("restaurantes/{id}/plato")
		Call<Void> updatePlato(@Path("id") String id, @Body Plato plato); 
		
		//@DELETE("restaurantes/{id}/plato")
		@FormUrlEncoded
		@HTTP(method = "DELETE", path = "restaurantes/{id}/plato", hasBody = true)
		Call<Void> removePlato(@Path("id") String id, @Field("nombre") String nombre);

}

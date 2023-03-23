package arso.mongodb;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.modelo.Valoracion;
import arso.opiniones.servicio.ServicioOpiniones;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;

public class Programa {

	public static void main(String[] args) throws RepositorioException, EntidadNoEncontradaException {

		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://arso:arso@cluster0.yhy3vkv.mongodb.net/?retryWrites=true&w=majority");

		CodecRegistry pojoCodecRegistry = 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = 
				CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.serverApi(ServerApi.builder()
						.version(ServerApiVersion.V1).build())
				.codecRegistry(codecRegistry)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Opinion> restaurantes = database.getCollection("opiniones", Opinion.class);
		
		ServicioOpiniones servicio = new ServicioOpiniones();
		String id1 = servicio.create("prueba");
		String id2 = servicio.create("prueba2");
		
		Valoracion v = new Valoracion();
		v.setCorreo("prueba");
		v.setCalificacion(2);
		servicio.addValoracion(id2, v);
				
		System.out.println(servicio.getOpinion(id1).toString());
		
		System.out.println(restaurantes.countDocuments());
		
		servicio.removeOpinion(id1);	
		
		System.out.println(restaurantes.countDocuments());

		System.out.println("fin.");
	}
}

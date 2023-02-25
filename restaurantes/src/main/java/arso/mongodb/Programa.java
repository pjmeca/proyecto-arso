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
import com.mongodb.client.model.Filters;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;

public class Programa {

	public static void main(String[] args) {

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
		MongoCollection<Restaurante> restaurantes = database.getCollection("restaurantes", Restaurante.class);
		
		Restaurante r1 = new Restaurante();
		r1.setNombre("Prueba");
		SitioTuristico sitio = new SitioTuristico();
		sitio.setNombre("SitioTuristico");
		r1.addSitioTuristico(sitio);
		
		restaurantes.insertOne(r1);
		
		System.out.println(restaurantes.countDocuments());
		
		System.out.println(restaurantes.find().first().getNombre());
		
		restaurantes.deleteOne(Filters.eq("nombre", "Prueba"));
		
		System.out.println(restaurantes.countDocuments());

		System.out.println("fin.");
	}
}

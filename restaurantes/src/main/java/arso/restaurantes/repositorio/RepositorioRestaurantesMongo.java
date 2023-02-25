package arso.restaurantes.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import arso.repositorio.EntidadNoEncontrada;
import arso.repositorio.RepositorioException;
import arso.repositorio.RepositorioString;
import arso.restaurantes.modelo.Restaurante;

public class RepositorioRestaurantesMongo implements RepositorioString<Restaurante>{
	
	private MongoCollection<Restaurante> restaurantes;
	
	public RepositorioRestaurantesMongo() {
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
		restaurantes = database.getCollection("restaurantes", Restaurante.class);
	}

	@Override
	public String add(Restaurante entity) throws RepositorioException {
		
		InsertOneResult result = restaurantes.insertOne(entity);
		
		if (result.getInsertedId() != null)
            return result.getInsertedId().asObjectId().getValue().toString();
        return null;

	}

	@Override
	public void update(Restaurante entity) throws RepositorioException, EntidadNoEncontrada {
		
		UpdateResult result = restaurantes.replaceOne(Filters.eq("id", entity.getId()), entity);
		
		if(result.getMatchedCount() == 0)
			throw new EntidadNoEncontrada("No se ha encontrado el restaurante con id: "+entity.getId());
	}

	@Override
	public void delete(Restaurante entity) throws RepositorioException, EntidadNoEncontrada {
		restaurantes.deleteOne(Filters.eq("id", entity.getId()));
		
	}

	@Override
	public Restaurante getById(String id) throws RepositorioException, EntidadNoEncontrada {
		return restaurantes.find(Filters.eq("id", id)).first();
	}

	@Override
	public List<Restaurante> getAll() throws RepositorioException {
		ArrayList<Restaurante> lista = new ArrayList<>();
		restaurantes.find().forEach(r -> {
			lista.add(r);
		});;
		return lista;
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		ArrayList<String> lista = new ArrayList<>();
		restaurantes.find().forEach(r -> {
			lista.add(r.getId());
		});;
		return lista;
	}

}

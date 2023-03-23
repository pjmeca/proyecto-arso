package arso.opiniones.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import arso.opiniones.modelo.Opinion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.repositorio.RepositorioString;

public class RepositorioOpinionMongo implements RepositorioString<Opinion>{
	
	private MongoCollection<Opinion> opiniones;
	
	public RepositorioOpinionMongo() {
		// ConnectionString connectionString = new ConnectionString(
		//		"mongodb+srv://arso:arso@cluster0.yhy3vkv.mongodb.net/?retryWrites=true&w=majority");
		
		// Para que jetty funcione hay que usar el driver 3.3
		ConnectionString connectionString = new ConnectionString(
				"mongodb://arso:arso@ac-muzzzdk-shard-00-00.yhy3vkv.mongodb.net:27017,ac-muzzzdk-shard-00-01.yhy3vkv.mongodb.net:27017,ac-muzzzdk-shard-00-02.yhy3vkv.mongodb.net:27017/?ssl=true&replicaSet=atlas-gvuc5f-shard-0&authSource=admin&retryWrites=true&w=majority");
				

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
		opiniones = database.getCollection("opiniones", Opinion.class);
	}

	@Override
	public String add(Opinion entity) throws RepositorioException {
		
		InsertOneResult result = opiniones.insertOne(entity);
		
		if (result.getInsertedId() != null)
			return result.getInsertedId().asObjectId().getValue().toString();
        return null;

	}

	@Override
	public void update(Opinion entity) throws RepositorioException, EntidadNoEncontradaException {
		
		if(entity.getId() == null || !ObjectId.isValid(entity.getId()))
			throw new EntidadNoEncontradaException("El id: "+entity.getId() +" no es válido.");
		
		//UpdateResult result = restaurantes.replaceOne(Filters.eq("id", entity.getId()), entity);
		UpdateResult result = opiniones.replaceOne(Filters.eq("_id", new ObjectId(entity.getId())), entity);
		
		if(result.getMatchedCount() == 0)
			throw new EntidadNoEncontradaException("No se ha encontrado la opinion con id: "+entity.getId());
	}

	@Override
	public void delete(Opinion entity) throws RepositorioException, EntidadNoEncontradaException {
		//restaurantes.deleteOne(Filters.eq("id", entity.getId()));
		DeleteResult resultado = opiniones.deleteOne(new Document("_id", entity.getId()));
		if(resultado.getDeletedCount() == 0)
			throw new EntidadNoEncontradaException("No existe la entidad.");
		
	}
	
	@Override
	public void delete(String entityId) throws RepositorioException, EntidadNoEncontradaException {
		DeleteResult resultado = opiniones.deleteOne(Filters.eq("_id", new ObjectId(entityId)));
		if(resultado.getDeletedCount() == 0)
			throw new EntidadNoEncontradaException("No existe la entidad.");
	}

	@Override
	public Opinion getById(String id) throws RepositorioException, EntidadNoEncontradaException {
		FindIterable<Opinion> r = opiniones.find(Filters.eq("_id", new ObjectId(id)));
		Opinion restaurante = r.first();
		
		if(restaurante == null)
			throw new EntidadNoEncontradaException("La opinion " + id + " no existe");
		return restaurante;
	}
	
	@Override
	public Opinion getByNombre(String nombre) throws RepositorioException, EntidadNoEncontradaException {
		FindIterable<Opinion> r = opiniones.find(Filters.eq("nombre", nombre));
		Opinion restaurante = r.first();
		
		if(restaurante == null)
			throw new EntidadNoEncontradaException("La opinion del recurso" + nombre + " no existe");
		return restaurante;
	}

	@Override
	public List<Opinion> getAll() throws RepositorioException {
		ArrayList<Opinion> lista = new ArrayList<>();
		opiniones.find().forEach(r -> {
			lista.add(r);
		});;
		return lista;
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		ArrayList<String> lista = new ArrayList<>();
		opiniones.find().forEach(r -> {
			lista.add(r.getId());
		});;
		return lista;
	}

}

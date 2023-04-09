using Repositorio;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using Opiniones.Modelo;
using Opiniones.Exceptions;

namespace Opiniones.Repositorio;

public class RepositorioOpinionesMongoDB : Repositorio<Opinion, string>
{
    private readonly IMongoCollection<Opinion> _opiniones;

    public RepositorioOpinionesMongoDB()
    {
        var client = new MongoClient("mongodb+srv://arso:arso@cluster0.yhy3vkv.mongodb.net/?retryWrites=true&w=majority");
        var database = client.GetDatabase("test");

        _opiniones = database.GetCollection<Opinion>("opiniones");
    }

    public string Add(Opinion entity)
    {
        if(string.IsNullOrWhiteSpace(entity.Id))
            throw new ArgumentException("El id: "+entity.Id +" no es válido.");

        _opiniones.InsertOne(entity);

        return entity.Id;
    }

    public void Update(Opinion entity)
    {
		if(string.IsNullOrWhiteSpace(entity.Id))
			throw new ArgumentException("El id: "+entity.Id +" no es válido.");
		
        var filter = Builders<Opinion>.Filter.Eq("_id", new ObjectId(entity.Id));
        var result = _opiniones.ReplaceOne(filter, entity);
		
		if(result.MatchedCount == 0)
			throw new EntidadNoEncontrada("No se ha encontrado la opinion con id: "+entity.Id);
	}

    public void Delete(Opinion entity)
    {
        if(string.IsNullOrWhiteSpace(entity.Id))
			throw new ArgumentException("El id: "+entity.Id +" no es válido.");

        var result = _opiniones.DeleteOne(opinion => opinion.Id == entity.Id);

        if(result.DeletedCount == 0)
			throw new EntidadNoEncontrada("No se ha encontrado la opinion con id: "+entity.Id);
    }

    public List<Opinion> GetAll()
    {
        return _opiniones.Find(_ => true).ToList();
    }

    public Opinion GetById(string id)
    {
        if(id is null || string.IsNullOrWhiteSpace(id) || !ObjectId.TryParse(id, out var objectId)){
			throw new ArgumentException("El id no es valido");
		}
        
        Opinion o = _opiniones
            .Find(opinion => opinion.Id == id)
            .FirstOrDefault();

        if(o is null)
            throw new EntidadNoEncontrada("No se ha encontrado ninguna opinión con id: "+id);

        return o;
    }

    public List<string> GetIds()
    {
        var todas = _opiniones.Find(_ => true).ToList();

        return todas.Select(p => p.Id).ToList();
    }
}

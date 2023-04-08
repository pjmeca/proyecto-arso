using Repositorio;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using Opiniones.Modelo;

namespace Opiniones.Repositorio;

public class RepositorioOpinionesMongoDB : Repositorio<Opinion, string>
{
    private readonly IMongoCollection<Opinion> opiniones;

    public RepositorioOpinionesMongoDB()
    {
        var client = new MongoClient("mongodb+srv://arso:arso@cluster0.yhy3vkv.mongodb.net/?retryWrites=true&w=majority");
        var database = client.GetDatabase("test");

        opiniones = database.GetCollection<Opinion>("opiniones");
    }

    public string Add(Opinion entity)
    {
        opiniones.InsertOne(entity);

        return entity.Id;
    }

    public void Delete(Opinion entity)
    {
        opiniones.DeleteOne(opinion => opinion.Id == entity.Id);
    }

    public List<Opinion> GetAll()
    {
        return opiniones.Find(_ => true).ToList();
    }

    public Opinion GetById(string id)
    {
        if(id is null || string.IsNullOrWhiteSpace(id)){
			throw new Exception("El id no es valido");
		}
        
        return opiniones
            .Find(opinion => opinion.Id == id)
            .FirstOrDefault();
    }

    public List<string> GetIds()
    {
        var todas = opiniones.Find(_ => true).ToList();

        return todas.Select(p => p.Id).ToList();

    }
}

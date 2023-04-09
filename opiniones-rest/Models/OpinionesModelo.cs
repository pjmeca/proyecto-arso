using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Opiniones.Modelo;

public class Opinion{

    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
	public String Id { get; set;}
	
    [BsonElement("nombre")]
	public String Nombre { get; set;}
    [BsonElement("valoraciones")]
	public List<Valoracion> Valoraciones { get; set;} = new();
    [BsonIgnore]
	public int NumValoraciones 
    {
        get { return Valoraciones.Count; }
    }
    [BsonIgnore]
	public float CalificacionMedia
    {
        get 
        {
            float nota = 0;
            foreach(Valoracion v in Valoraciones){
                nota+=v.Calificacion;
            }
            
            return Valoraciones.Count == 0 ? 1 : nota/Valoraciones.Count;            
        }
    }
	
	public void AddValoracion(Valoracion valoracion){
        if(valoracion is null || !valoracion.IsValid())
            throw new ArgumentException("Valoracion no es válida");
            
		Valoraciones.RemoveAll(v => v.Correo is not null && v.Correo.Equals(valoracion.Correo));
		Valoraciones.Add(valoracion);		
	}	
}


public class Valoracion {
    [BsonElement("correo")]
	public string Correo { get; set;}
    [BsonElement("fecha")]
	public DateTime Fecha { get; set;} = DateTime.Now;
    private float calificacion;
    [BsonElement("calificacion")]
	public float Calificacion 
    {
        get 
        {
            return calificacion;
        }

        set 
        {
            if(value < 1 || value > 5)
			    throw new ArgumentException("La calificación debe encontrarse entre 1 y 5."); // IllegalArgumentException
		
		    calificacion = value;
        }
    }
    [BsonElement("comentario")]
	public string? Comentario { get; set;}

    public bool IsValid()
    {
        return !string.IsNullOrWhiteSpace(Correo) && Calificacion != 0;
    }
}
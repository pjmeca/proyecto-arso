using Eventos;
using Opiniones.Modelo;
using Repositorio;

namespace Opiniones.Servicio;

public interface IServicioOpiniones
{
    string Create(string nombre);
    Opinion Get(string id);
    void Delete(Opinion opinion);
    void AddValoracion(string id, Valoracion valoracion);
    List<Opinion> GetAll();
    List<Valoracion> GetValoraciones(string id);
}

public class ServicioOpiniones : IServicioOpiniones
{
    private Repositorio<Opinion, String> _repositorio { get; set; }

    public ServicioOpiniones(Repositorio<Opinion, String> repositorio)
    {
        this._repositorio = repositorio;
    }

    public string Create(string nombre)
    {
        if(string.IsNullOrWhiteSpace(nombre))
            throw new ArgumentException("El nombre: "+ nombre +" no es válido.");

        Opinion opinion = new()
        {
            Nombre = nombre
        };

        return this._repositorio.Add(opinion);
    }

    public Opinion Get(string id)
    {
        return this._repositorio.GetById(id);
    }

    public void Delete(Opinion opinion)
    {
		_repositorio.Delete(opinion);	
    }

    public List<Opinion> GetAll() 
    {        
        return _repositorio.GetAll();
    }

    public void AddValoracion(string id, Valoracion valoracion) 
    {		
		Opinion opinion = Get(id);
		opinion.AddValoracion(valoracion);
		
		_repositorio.Update(opinion);

        EventoServicio.send(
            new() 
            {
                IdOpinion = opinion.Id,
                Valoracion = valoracion,
                NumValoraciones = opinion.NumValoraciones,
                CalMedia = opinion.CalificacionMedia
            }
        );
	}

    public List<Valoracion> GetValoraciones(string id) 
    {
        Opinion opinion = Get(id);
        return opinion.Valoraciones;
    }
}
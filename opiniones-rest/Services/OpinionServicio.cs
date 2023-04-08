using Opiniones.Modelo;
using Repositorio;

namespace Opiniones.Servicio;

public interface IServicioOpiniones
{
    string Create(string nombre);
    Opinion Get(string id);
    void Delete(Opinion opinion);
    List<Opinion> GetAll();
}

public class ServicioOpiniones : IServicioOpiniones
{
    private Repositorio<Opinion, String> repositorio { get; set; }
    public ServicioOpiniones(Repositorio<Opinion, String> repositorio)
    {
        this.repositorio = repositorio;
    }

    public string Create(string nombre)
    {
        Opinion opinion = new()
        {
            Nombre = nombre
        };

        return this.repositorio.Add(opinion);
    }

    public Opinion Get(string id)
    {
        return this.repositorio.GetById(id);
    }

    public void Delete(Opinion opinion)
    {
		repositorio.Delete(opinion);	
    }

    public List<Opinion> GetAll() 
    {
        return this.repositorio.GetAll();
    }
}
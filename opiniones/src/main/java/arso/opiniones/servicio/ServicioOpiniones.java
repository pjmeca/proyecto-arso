package arso.opiniones.servicio;

import java.util.List;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.FactoriaRepositorios;
import arso.repositorio.Repositorio;
import arso.repositorio.RepositorioException;

public class ServicioOpiniones implements IServicioOpiniones {

	private Repositorio<Opinion, String> repositorio;
	
	public ServicioOpiniones() {
		repositorio = FactoriaRepositorios.getRepositorio(Opinion.class);
	}
	
	private boolean contiene(String recurso) {
		try {
			repositorio.getByNombre(recurso);
			return true;				
		} catch (RepositorioException | EntidadNoEncontradaException e) {
			return false;
		}
	}
	
	@Override
	public String create(String recurso) throws RepositorioException {
		if(recurso.isBlank())
			throw new RepositorioException("El nombre del recurso no es válido.");
		if(contiene(recurso))
			throw new RepositorioException("El recurso ya está registrado.");
			
		Opinion o = new Opinion();
		o.setNombre(recurso);
		return repositorio.add(o);
	}
	
	@Override
	public Opinion crear(String recurso)  throws RepositorioException, EntidadNoEncontradaException {
		return getOpinion(create(recurso));
	}

	@Override
	public void addValoracion(String id, Valoracion valoracion) throws RepositorioException, EntidadNoEncontradaException {
		if(valoracion == null)
			throw new RepositorioException("La valoracion no puede ser nula.");
		
		Opinion opinion = getOpinion(id);
		Boolean resultado = opinion.addValoracion(valoracion);
		
		if(!resultado)
			throw new RepositorioException("No se ha podido guardar la valoracion");
		
		repositorio.update(opinion);
	}
	
	@Override
	public Opinion getOpinion(String id) throws RepositorioException, EntidadNoEncontradaException {
		if(id == null || id.isBlank()) // Mejor comprobarlo aquí para evitar llamadas al repositorio
			throw new RepositorioException("El id está vacío.");
		return repositorio.getById(id);
	}

	

	@Override
	public void removeOpinion(String id) throws RepositorioException, EntidadNoEncontradaException {
		if(id==null | id.isBlank()){
			throw new IllegalArgumentException("El id no es valido");
		}
		repositorio.delete(id);		
	}
	
	@Override
	public List<Opinion> findAll() throws RepositorioException{
		return repositorio.getAll();
	}
	
}

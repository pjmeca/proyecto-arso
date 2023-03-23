package opiniones.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.modelo.Valoracion;
import arso.opiniones.servicio.IServicioOpiniones;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.servicio.FactoriaServicios;

public class Mutation implements GraphQLRootResolver {
    
	private IServicioOpiniones servicio = FactoriaServicios.getServicio(IServicioOpiniones.class);
	
    public Opinion crearOpinion(String recurso) throws RepositorioException, EntidadNoEncontradaException {
        
    	return servicio.crear(recurso);
    }
    
    public String addValoracion(String id, Valoracion valoracion) throws RepositorioException, EntidadNoEncontradaException {
		servicio.addValoracion(id, valoracion);
		return id;
	}
	
	public String removeOpinion(String id) throws RepositorioException, EntidadNoEncontradaException{
		servicio.removeOpinion(id);
		return id;
	}
}

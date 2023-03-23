package opiniones.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.servicio.IServicioOpiniones;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.servicio.FactoriaServicios;

public class Query implements GraphQLRootResolver {
    
	private IServicioOpiniones servicio = FactoriaServicios.getServicio(IServicioOpiniones.class);
	
	public Opinion getOpinion(String id) throws RepositorioException, EntidadNoEncontradaException{
		return servicio.getOpinion(id);
	}
	
    public List<Opinion> findAll() throws RepositorioException {
        return servicio.findAll();
    }
}
package opiniones.graphql;

import java.time.LocalDateTime;

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
    
    public Valoracion addValoracion(String id, String correo, String fecha, float calificacion, String comentario) throws RepositorioException, EntidadNoEncontradaException {
		Valoracion valoracion = new Valoracion();
		valoracion.setCorreo(correo);
		valoracion.setFecha(LocalDateTime.parse(fecha));
		valoracion.setCalificacion(calificacion);
		valoracion.setComentario(comentario);
    	servicio.addValoracion(id, valoracion);
		return valoracion;
	}
	
	public String removeOpinion(String id) throws RepositorioException, EntidadNoEncontradaException{
		servicio.removeOpinion(id);
		return id;
	}
}

package arso.opiniones.servicio;

import java.util.List;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.RepositorioException;

public interface IServicioOpiniones {
	
	
	/** 
	 * Metodo para registrar un recurso.
	 * 
	 * @param idRestaurante del restaurante que se dara de alta en las valoraciones
	 */
	public String altaOpiniones(String idRestaurante) throws RepositorioException;
	
	
	/** 
	 * Metodo para registrar un recurso.
	 * 
	 * @param idRestaurante el restaurante del que se recuperaran sus valoraciones
	 * @return lista con las valoraciones del restaurante
	 */
	public List<Valoracion> getValoraciones(String idRestaurante) throws RepositorioException;

	/**
	 * Método para eliminar la opinión asociada a un recurso.
	 * @param id el id de la opinión
	 */
	public void delete(String id) throws RepositorioException;
}

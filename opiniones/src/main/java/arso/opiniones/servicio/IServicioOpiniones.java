package arso.opiniones.servicio;

import java.util.List;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;

public interface IServicioOpiniones {

	/** 
	 * Metodo para registrar un recurso.
	 * 
	 * @param nombre del recurso que será valorado
	 * @return identificador de la opinión
	 */
	String create(String recurso) throws RepositorioException;
	
	/**
	 * Método para registrar un recurso (crear una opinión).
	 * @return el objeto opinión
	 */
	public Opinion crear(String recurso)  throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Añade una valoración sobre un recurso.
	 * @param id identificador de la opinión del recurso a valorar
	 * @param valoracion (reemplazará a la actual en caso de existir)
	 */
	void addValoracion(String id, Valoracion valoracion) throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Recupera una Opinion utilizando el identificador. 	
	 */
	Opinion getOpinion(String id)  throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Elimina una opinión del servicio y sus valoraciones.
	 * @param id de la opinión
	 */
	void removeOpinion(String id)  throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Devuelve todas las opiniones
	 */
	List<Opinion> findAll() throws RepositorioException; 
	
}

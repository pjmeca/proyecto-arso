package arso.opiniones.servicio;

import arso.opiniones.modelo.Opinion;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;

public interface IServicioOpiniones {

	/** 
	 * Metodo para registrar un recuro.
	 * 
	 * @param nombre del recurso que será valorado
	 * @return identificador de la opinión
	 */
	String create(String recurso) throws RepositorioException;
	
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
	
}

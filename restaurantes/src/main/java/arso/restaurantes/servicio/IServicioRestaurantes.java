package arso.restaurantes.servicio;

import java.util.List;

import arso.especificacion.Specification;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;

public interface IServicioRestaurantes {

	/** 
	 * Metodo de alta de un Restaurante.
	 * 
	 * @param Restaurante debe ser valido respecto al modelo de dominio
	 * @return identificador del Restaurante
	 */
	String create(Restaurante restaurante) throws RepositorioException;
	
	/**
	 * Actualiza un Restaurante.
	 * @param Restaurante debe ser valido respecto al modelo de dominio
	 */
	void update(Restaurante restaurante) throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Recupera un Restaurante utilizando el identificador. 	
	 */
	Restaurante getRestaurante(String id)  throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Obtiene una lista de sitios próximos a un restaurante.
	 * @param El id debe pertenecer a un restaurante debe ser valido respecto al modelo de dominio
	 */
	List<SitioTuristico> getSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Establece la lista de sitios turisticos destacados del restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 */
	void setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontradaException ;
	
	/**
	 * Añade un plato al restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param El plato debe contener un nombre único en el restaurante
	 */
	void addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontradaException ;
	
	/**
	 * Borra un plato del restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param El nombre del plato debe ser único en el restaurante
	 */
	void removePlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontradaException ;
	
	/**
	 * Actualiza un plato al restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param Plato debe contener un nombre único en el restaurante y formar parte del mismo
	 */
	void updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontradaException;
	
	
	/**
	 * Elimina un Restaurante.
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 */
	void removeRestaurante(String idRestaurante)  throws RepositorioException, EntidadNoEncontradaException;
	
	/*
	 * Elimina todos los restaurantes del repositorio.
	 */
	void removeAll()  throws RepositorioException, EntidadNoEncontradaException;
	
	/**
	 * Retorna un resumen de todos los Restaurantes.	
	 */
	List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException;
	
	/**
	 * Retorna un resumen de todos los Restaurantes siguiendo el filtro.	
	 */
	List<RestauranteResumen> getListadoRestaurantesBySpecification(Specification<Restaurante> especificacion) throws RepositorioException, EntidadNoEncontradaException;
}

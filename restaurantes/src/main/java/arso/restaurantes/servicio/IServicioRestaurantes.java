package arso.restaurantes.servicio;

import java.util.List;
import arso.repositorio.EntidadNoEncontrada;
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
	void update(Restaurante restaurante) throws RepositorioException, EntidadNoEncontrada;
	
	/**
	 * Recupera un Restaurante utilizando el identificador. 	
	 */
	Restaurante getRestaurante(String id)  throws RepositorioException, EntidadNoEncontrada;
	
	/**
	 * Obtiene una lista de sitios próximos a un restaurante.
	 * @param El id debe pertenecer a un restaurante debe ser valido respecto al modelo de dominio
	 */
	List<SitioTuristico> getSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;
	
	/**
	 * Establece la lista de sitios turisticos destacados del restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 */
	void setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada ;
	
	/**
	 * Añade un plato al restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param El plato debe contener un nombre único en el restaurante
	 */
	void addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada ;
	
	/**
	 * Borra un plato del restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param El nombre del plato debe ser único en el restaurante
	 */
	void removePlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontrada ;
	
	/**
	 * Actualiza un plato al restaurante
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 * @param Plato debe contener un nombre único en el restaurante y formar parte del mismo
	 */
	void updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada;
	
	
	/**
	 * Elimina un Restaurante.
	 * @param El id debe pertenecer a un restaurante valido respecto al modelo de dominio
	 */
	void removeRestaurante(String idRestaurante)  throws RepositorioException, EntidadNoEncontrada;
	
	/**
	 * Retorna un resumen de todos los Restaurantees.	
	 */
	List<RestauranteResumen> getListadoRestaurantees() throws RepositorioException;
}

package arso.repositorio;

import java.util.List;

/*
 *  Repositorio para entidades gestionadas con identificador.
 *  El parámetro T representa al tipo de datos de la entidad.
 *  El parámetro K es el tipo del identificador.
 */

public interface Repositorio <T, K> {
    
		
    K add(T entity) throws RepositorioException;
    
    void update(T entity) throws RepositorioException, EntidadNoEncontradaException;
    
    void delete(T entity) throws RepositorioException, EntidadNoEncontradaException;
    
    void delete(String entityId) throws RepositorioException, EntidadNoEncontradaException;

    T getById(K id) throws RepositorioException, EntidadNoEncontradaException;
    
    T getByNombre(K nombre) throws RepositorioException, EntidadNoEncontradaException;
    
	List<T> getAll() throws RepositorioException;
	
	default void removeAll() throws RepositorioException, EntidadNoEncontradaException{
		List<T> lista = getAll();
		for(T t : lista)
			delete(t);
	}

	List<K> getIds() throws RepositorioException;	
}

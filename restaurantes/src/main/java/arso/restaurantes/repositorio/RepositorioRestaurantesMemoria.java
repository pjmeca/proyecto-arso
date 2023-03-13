package arso.restaurantes.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import arso.especificacion.Specification;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.repositorio.RepositorioString;
import arso.restaurantes.modelo.Restaurante;
import arso.utils.Utils;

public class RepositorioRestaurantesMemoria implements RepositorioString<Restaurante>{
	
	private HashMap<String, Restaurante> mapa = new HashMap<>();
	private static int contadorId = 0;

	@Override
	public String add(Restaurante entity) throws RepositorioException {
		
		//String id = Utils.createId();
		String id = ++contadorId + ""; // para las pruebas, mejor que cree los id secuencialmente
		
		entity.setId(id);
		mapa.put(id, entity);
		
		return id;
	}

	@Override
	public void update(Restaurante entity) throws RepositorioException, EntidadNoEncontradaException {
		
		if(!mapa.containsKey(entity.getId()))
			throw new EntidadNoEncontradaException(entity.getId() + " no existe");
		
		mapa.put(entity.getId(), entity);
		
	}

	@Override
	public void delete(Restaurante entity) throws RepositorioException, EntidadNoEncontradaException {
		mapa.remove(entity.getId());
		
	}
	
	@Override
	public void delete(String entityId) throws RepositorioException, EntidadNoEncontradaException {
		if(mapa.remove(entityId) == null)
			throw new EntidadNoEncontradaException("No existe la entidad.");
		
	}

	@Override
	public Restaurante getById(String id) throws RepositorioException, EntidadNoEncontradaException {
		if(!mapa.containsKey(id))
			throw new EntidadNoEncontradaException(id + " no existe");
				
		return mapa.get(id).clone();
	}
	
	@Override
	public List<Restaurante> getBySpecification(Specification<Restaurante> especificacion){
		
		return mapa.values().stream().filter(r -> r.satisfies(especificacion)).toList();
	}

	@Override
	public List<Restaurante> getAll() throws RepositorioException {
		return new ArrayList<>(mapa.values());
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		return new ArrayList<>(mapa.keySet());
	}
}

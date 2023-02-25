package arso.restaurantes.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import arso.repositorio.EntidadNoEncontrada;
import arso.repositorio.RepositorioException;
import arso.repositorio.RepositorioString;
import arso.restaurantes.modelo.Restaurante;
import arso.utils.Utils;

public class RepositorioRestaurantesMemoria implements RepositorioString<Restaurante>{
	
	private HashMap<String, Restaurante> mapa = new HashMap<>();

	@Override
	public String add(Restaurante entity) throws RepositorioException {
		
		String id = Utils.createId();
		
		entity.setId(id);
		mapa.put(id, entity);
		
		return id;
	}

	@Override
	public void update(Restaurante entity) throws RepositorioException, EntidadNoEncontrada {
		
		if(!mapa.containsKey(entity.getId()))
			throw new EntidadNoEncontrada(entity.getId() + " no existe");
		
		mapa.put(entity.getId(), entity);
		
	}

	@Override
	public void delete(Restaurante entity) throws RepositorioException, EntidadNoEncontrada {
		mapa.remove(entity.getId());
		
	}

	@Override
	public Restaurante getById(String id) throws RepositorioException, EntidadNoEncontrada {
		return mapa.get(id);
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

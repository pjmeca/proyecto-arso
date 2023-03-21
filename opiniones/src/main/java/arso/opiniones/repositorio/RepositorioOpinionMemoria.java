package arso.opiniones.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import arso.opiniones.modelo.Opinion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.repositorio.RepositorioString;

public class RepositorioOpinionMemoria implements RepositorioString<Opinion>{
	
	private HashMap<String, Opinion> mapa = new HashMap<>();

	@Override
	public String add(Opinion entity) throws RepositorioException {
		
		//String id = Utils.createId();
		String id = entity.getId();
		mapa.put(id, entity);
		
		return id;
	}

	@Override
	public void update(Opinion entity) throws RepositorioException, EntidadNoEncontradaException {
		
		if(!mapa.containsKey(entity.getId()))
			throw new EntidadNoEncontradaException(entity.getId() + " no existe");
		
		mapa.put(entity.getId(), entity);
		
	}

	@Override
	public void delete(Opinion entity) throws RepositorioException, EntidadNoEncontradaException {
		mapa.remove(entity.getId());
		
	}
	
	@Override
	public void delete(String entityId) throws RepositorioException, EntidadNoEncontradaException {
		if(mapa.remove(entityId) == null)
			throw new EntidadNoEncontradaException("No existe la entidad.");
		
	}

	@Override
	public Opinion getById(String id) throws RepositorioException, EntidadNoEncontradaException {
		if(!mapa.containsKey(id))
			throw new EntidadNoEncontradaException(id + " no existe");
				
		return mapa.get(id).clone();
	}

	@Override
	public List<Opinion> getAll() throws RepositorioException {
		return new ArrayList<>(mapa.values());
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		return new ArrayList<>(mapa.keySet());
	}
}

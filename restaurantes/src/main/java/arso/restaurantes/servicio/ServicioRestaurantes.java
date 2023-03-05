package arso.restaurantes.servicio;

import java.util.ArrayList;
import java.util.List;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.FactoriaRepositorios;
import arso.repositorio.Repositorio;
import arso.repositorio.RepositorioException;
import arso.restaurantes.dom.BuscadorGeoNames;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;

public class ServicioRestaurantes implements IServicioRestaurantes {

	private Repositorio<Restaurante, String> repositorio;
	
	public ServicioRestaurantes() {
		repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
	}

	@Override
	public String create(Restaurante restaurante) throws RepositorioException {
		if(restaurante == null)
			throw new RepositorioException("El restaurante es nulo.");
		if(restaurante.getNombre() == null || restaurante.getNombre().isBlank())
			throw new RepositorioException("El restaurante no tiene nombre.");
		if(restaurante.getId() != null) {
			boolean lanzada = false;
			try {
				getRestaurante(restaurante.getId());
				lanzada = true;				
			} catch (RepositorioException | EntidadNoEncontradaException e) {}
			
			if(lanzada)
				throw new RepositorioException("El restaurante ya existe en el repositorio.");
		}
			
		return repositorio.add(restaurante);
	}

	@Override
	public void update(Restaurante restaurante) throws RepositorioException, EntidadNoEncontradaException {
		if(restaurante == null)
			throw new RepositorioException("El restaurante es nulo.");
		if(restaurante.getNombre()==null)
			throw new RepositorioException("El restaurante no tiene nombre.");
		repositorio.update(restaurante);		
	}

	@Override
	public Restaurante getRestaurante(String id) throws RepositorioException, EntidadNoEncontradaException {
		if(id == null || id.isBlank()) // Mejor comprobarlo aquí para evitar llamadas al repositorio
			throw new RepositorioException("El id está vacío.");
		return repositorio.getById(id);
	}

	@Override
	public List<SitioTuristico> getSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException {
		Restaurante restaurante = getRestaurante(idRestaurante);
		double lat = restaurante.getLatitud();
		double lng = restaurante.getLongitud();
		return BuscadorGeoNames.getInstance().findByCoord(lat, lng);
	}

	@Override
	public void setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontradaException {
		if(sitios == null)
			throw new RepositorioException("La lista de sitios turísticos no puede ser nulo.");
		
		Restaurante restaurante = getRestaurante(idRestaurante);
		restaurante.setSitiosTuristicos(sitios);
		update(restaurante);
	}

	@Override
	public void addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontradaException {
		if(plato == null)
			throw new RepositorioException("El plato no puede ser nulo.");
		
		Restaurante restaurante = getRestaurante(idRestaurante);
		Boolean resultado = restaurante.addPlato(plato);
		
		if(!resultado)
			throw new RepositorioException("Ya existe un plato con nombre "+ plato.getNombre() + " en el restaurante");
		
		update(restaurante);
	}

	@Override
	public void removePlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontradaException {
		Restaurante restaurante = getRestaurante(idRestaurante);
		if(!restaurante.removePlato(nombrePlato))
			throw new RepositorioException("No existe nigún plato con nombre" + nombrePlato + " en el restaurante");
		update(restaurante);
	}

	@Override
	public void updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontradaException{
		if(plato == null)
			throw new RepositorioException("El plato no puede ser nulo.");
		
		Restaurante restaurante = getRestaurante(idRestaurante);
		if(!restaurante.updatePlato(plato))
			throw new RepositorioException("No existe nigún plato con nombre" + plato.getNombre() + " en el restaurante");	
	}

	@Override
	public void removeRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException {
		Restaurante restaurante = getRestaurante(idRestaurante);
		repositorio.delete(restaurante);		
	}

	@Override
	public List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException {
		
		List<RestauranteResumen> resultado = new ArrayList<>();

		
		for (String id : repositorio.getIds()) {
			try {
				Restaurante restaurante = getRestaurante(id);
				RestauranteResumen resumen = new RestauranteResumen();
				resumen.setId(restaurante.getId());
				resumen.setNombre(restaurante.getNombre());
				resumen.setLatitud(restaurante.getLatitud());
				resumen.setLongitud(restaurante.getLongitud());
				resultado.add(resumen);
				
			} catch (Exception e) {
				// No debe suceder
				e.printStackTrace(); // para depurar
			}
		}

		return resultado;
	}
}

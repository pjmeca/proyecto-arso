package arso.restaurantes.servicio;

import java.util.ArrayList;
import java.util.List;
import arso.repositorio.EntidadNoEncontrada;
import arso.repositorio.FactoriaRepositorios;
import arso.repositorio.Repositorio;
import arso.repositorio.RepositorioException;
import arso.restaurantes.dom.BuscadorGeoNames;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;

public class ServicioRestaurantes implements IServicioRestaurantes {

	private Repositorio<Restaurante, String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);

	@Override
	public String create(Restaurante restaurante) throws RepositorioException {
		return repositorio.add(restaurante);
	}

	@Override
	public void update(Restaurante restaurante) throws RepositorioException, EntidadNoEncontrada {
		repositorio.update(restaurante);		
	}

	@Override
	public Restaurante getRestaurante(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}

	@Override
	public List<SitioTuristico> getSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = getRestaurante(idRestaurante);
		double lat = restaurante.getLatitud();
		double lng = restaurante.getLongitud();
		return BuscadorGeoNames.getInstance().findByCoord(lat, lng);
	}

	@Override
	public void setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = getRestaurante(idRestaurante);
		restaurante.setSitiosTuristicos(sitios);
		update(restaurante);
	}

	@Override
	public void addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = getRestaurante(idRestaurante);
		Boolean resultado = restaurante.addPlato(plato);
		if(!resultado)
			throw new RepositorioException("Ya existe un plato con nombre "+ plato.getNombre() + " en el restaurante");
		update(restaurante);
	}

	@Override
	public void removePlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = getRestaurante(idRestaurante);
		if(!restaurante.removePlato(nombrePlato))
			throw new RepositorioException("No existe nigún plato con nombre" + nombrePlato + " en el restaurante");
		update(restaurante);
	}

	@Override
	public void updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada{
		Restaurante restaurante = getRestaurante(idRestaurante);
		if(!restaurante.updatePlato(plato))
			throw new RepositorioException("No existe nigún plato con nombre" + plato.getNombre() + " en el restaurante");	
	}

	@Override
	public void removeRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = getRestaurante(idRestaurante);
		repositorio.delete(restaurante);		
	}

	@Override
	public List<RestauranteResumen> getListadoRestaurantees() throws RepositorioException {
		
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

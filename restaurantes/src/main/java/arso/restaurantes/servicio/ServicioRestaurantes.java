package arso.restaurantes.servicio;

import java.util.ArrayList;
import java.util.List;
import arso.especificacion.Specification;
import arso.opiniones.modelo.Valoracion;
import arso.opiniones.servicio.IServicioOpiniones;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.FactoriaRepositorios;
import arso.repositorio.Repositorio;
import arso.repositorio.RepositorioException;
import arso.restaurantes.dom.BuscadorGeoNames;
import arso.restaurantes.especificacion.IsOpinionSpecification;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;
import arso.servicio.FactoriaServicios;

public class ServicioRestaurantes implements IServicioRestaurantes {

	private Repositorio<Restaurante, String> repositorio;
	private IServicioOpiniones opiniones = FactoriaServicios.getServicio(IServicioOpiniones.class); 
	
	public ServicioRestaurantes() {
		repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
	}

	private boolean contiene(String restauranteId) {
		try {
			getRestaurante(restauranteId);
			return true;				
		} catch (RepositorioException | EntidadNoEncontradaException e) {
			return false;
		}
	}
	
	@Override
	public String create(Restaurante restaurante) throws RepositorioException {
		if(restaurante == null)
			throw new RepositorioException("El restaurante es nulo.");
		if(restaurante.getNombre() == null || restaurante.getNombre().isBlank())
			throw new RepositorioException("El restaurante no tiene nombre.");
		if(restaurante.getId() != null) {
			if(contiene(restaurante.getId()))
				throw new RepositorioException("El restaurante ya existe.");
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
		Restaurante r = repositorio.getById(id);
		return r;
	}
	
	@Override
	public void updateResumenOpinion(String idOpinion, int nValoraciones, float calMedia) throws RepositorioException, EntidadNoEncontradaException {
		List<Restaurante> lista = repositorio.getBySpecification(new IsOpinionSpecification(idOpinion));
		
		if(lista.size() > 1)
			throw new RepositorioException("Más de un restaurante tiene la opinión: " + idOpinion);
		
		for(Restaurante r : lista) {
			System.out.println("Actualizando el resumen de: " + r.getNombre());
			r.setNumValoraciones(nValoraciones);
			r.setCalificacionMedia(calMedia);
			update(r);
		}				
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
	public boolean contienePlato(String idRestaurante, String nombre) throws RepositorioException, EntidadNoEncontradaException {
		Restaurante restaurante = getRestaurante(idRestaurante);
		return restaurante.getPlato(nombre) != null;
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
			throw new EntidadNoEncontradaException("No existe nigún plato con nombre" + plato.getNombre() + " en el restaurante");
		update(restaurante);
	}

	@Override
	public void removeRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException {
		if(idRestaurante==null | idRestaurante.isBlank()){
			throw new IllegalArgumentException("El id no es valido");
		}
		repositorio.delete(idRestaurante);		
	}
	
	@Override
	public void removeAll() throws RepositorioException, EntidadNoEncontradaException {
		repositorio.removeAll();
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

	@Override
	public List<RestauranteResumen> getListadoRestaurantesBySpecification(Specification<Restaurante> especificacion)
			throws RepositorioException, EntidadNoEncontradaException {
		if(especificacion==null)
			throw new RepositorioException("La especificacion no puede ser nula");
		
		List<RestauranteResumen> resultado = new ArrayList<>();
		List<Restaurante> restaurantes = repositorio.getBySpecification(especificacion);		
		
		for(Restaurante r : restaurantes) {
			RestauranteResumen resumen = new RestauranteResumen();
			resumen.setId(r.getId());
			resumen.setNombre(r.getNombre());
			resumen.setLatitud(r.getLatitud());
			resumen.setLongitud(r.getLongitud());
			resultado.add(resumen);
		}
		
		return resultado;
	}
	
	@Override
	public void altaOpiniones(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException {
		Restaurante r = getRestaurante(idRestaurante);
		
		String idOpinion = opiniones.altaOpiniones(r.getNombre());
		
		r.setOpinion(idOpinion);		
		r.setCalificacionMedia(1);
		r.setNumValoraciones(0);
		update(r);
	}
	
	@Override
	public List<Valoracion> getValoraciones(String idRestaurante) throws RepositorioException, EntidadNoEncontradaException{
		Restaurante r = getRestaurante(idRestaurante);
		
		if(r.getOpinion() == null || r.getOpinion().isBlank())
			throw new RepositorioException("El restaurante: " + idRestaurante + " no se ha registrado en el servicio opiniones.");
		
		return opiniones.getValoraciones(r.getOpinion());
	}
	
}

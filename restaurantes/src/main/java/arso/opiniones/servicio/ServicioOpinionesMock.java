package arso.opiniones.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import arso.opiniones.modelo.Valoracion;
import arso.repositorio.RepositorioException;

public class ServicioOpinionesMock implements IServicioOpiniones {

	private String idPrueba = "641ca986c146350c5d92d66d";
	
	private List<String> recursos = new ArrayList<>();
	
	
	@Override
	public String altaOpiniones(String idRestaurante) throws RepositorioException{
		if(idRestaurante.isBlank())
			throw new RepositorioException("El nombre del recurso no es válido.");
		if(recursos.contains(idRestaurante))
			throw new RepositorioException("El recurso ya está registrado.");
		
		recursos.add(idRestaurante);
		
		return idPrueba;
	}
	
	
	@Override
	public List<Valoracion> getValoraciones(String idOpinion) throws RepositorioException{		
		if(idOpinion.isBlank())
			throw new RepositorioException("El nombre del recurso no es válido.");
		if(!idOpinion.equals(idPrueba))
			throw new RepositorioException("El recurso no está registrado.");
			
		List<Valoracion> lista = new ArrayList<>();
		
		Valoracion v = new Valoracion();
		v.setCorreo("prueba");
		v.setComentario("prueba");
		v.setCalificacion(4);
		v.setFecha(LocalDateTime.now());		
		lista.add(v);
		
		return lista;
	}

}

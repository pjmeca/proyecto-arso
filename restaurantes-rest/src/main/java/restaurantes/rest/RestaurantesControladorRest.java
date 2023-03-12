package restaurantes.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import arso.especificacion.AndSpecification;
import arso.especificacion.Specification;
import arso.repositorio.EntidadNoEncontradaException;
import arso.restaurantes.especificacion.IsContienePlatoSpecification;
import arso.restaurantes.especificacion.IsRadioSpecification;
import arso.restaurantes.modelo.Plato;
import arso.restaurantes.modelo.Restaurante;
import arso.restaurantes.modelo.SitioTuristico;
import arso.restaurantes.servicio.IServicioRestaurantes;
import arso.restaurantes.servicio.RestauranteResumen;
import arso.servicio.FactoriaServicios;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import restaurantes.rest.ListadoResumenRestaurantes.ResumenExtendido;

@Api
@Path("restaurantes")

public class RestaurantesControladorRest {

	private IServicioRestaurantes servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class);

	@Context
	private UriInfo uriInfo;
	
	// Ejemplo: http://localhost:8080/api/restaurantes/1
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta un restaurante", notes = "Retorna un restaurante utilizando su id", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response getRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws Exception {

		return Response.status(Response.Status.OK).entity(servicio.getRestaurante(id)).build();
	}

	// Utiliza un fichero de prueba disponible en el proyecto
	// curl -i -X POST -H "Content-type: application/xml" -d @test-files/1.xml http://localhost:8080/api/restaurantes/

	// No hay que agregar ningún fragmento al path

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response create(Restaurante restaurante) throws Exception {

		String id = servicio.create(restaurante);

		URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(nuevaURL).build();
	}

	// Utiliza un fichero de prueba disponible en el proyecto
	// curl -i -X PUT -H "Content-type: application/xml" -d @test-files/1.xml http://localhost:8080/api/restaurantes/1

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response update(@PathParam("id") String id, Restaurante restaurante) throws Exception {

		if (!id.equals(restaurante.getId()))
			throw new IllegalArgumentException("El identificador no coincide: " + id);

		servicio.update(restaurante);

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// curl -i -X DELETE http://localhost:8080/api/restaurantes/1

	@DELETE
	@Path("/{id}")
	public Response removeRestaurante(@PathParam("id") String id) throws Exception {

		servicio.removeRestaurante(id);

		return Response.status(Response.Status.NO_CONTENT).build();

	}
	
	
	@GET
	@Path("/{id}/sitiosProximos")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Obtiene la lista de sitios turísticos próximos al restaurante", notes = "Retorna los sitios próximos de un restaurante utilizando su id", response = SitioTuristico.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response getSitiosProximosRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws Exception {

		ListadoSitioTuristico l = new ListadoSitioTuristico();
		l.set(servicio.getSitiosTuristicosProximos(id));
		return Response.ok(l,MediaType.APPLICATION_XML).build();
	}

	private Response getListadoRestaurantes(List<RestauranteResumen> resultado) throws Exception {
 
		LinkedList<ResumenExtendido> extendido = new LinkedList<>();

		for (RestauranteResumen rResumen : resultado) {

			ResumenExtendido resumenExtendido = new ResumenExtendido();
			resumenExtendido.setResumen(rResumen);

			// URL
			String id = rResumen.getId();
			URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();
			resumenExtendido.setUrl(nuevaURL.toString()); // string
			extendido.add(resumenExtendido);
		}

		// Una lista no es un documento válido en XML
		// Creamos un documento con un envoltorio
		ListadoResumenRestaurantes listado = new ListadoResumenRestaurantes();
		listado.setRestaurantes(extendido);

		return Response.ok(listado).build();
	}
	
	// Si no se especifica la cabecera "Accept", se retorna en el primer formato (XML)
	// curl http://localhost:8080/api/restaurantes

	// curl -H "Accept: application/json" http://localhost:8080/api/restaurantes
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getListadoRestaurantes(
			@QueryParam("radio") @DefaultValue("-1") int radio, 
            @QueryParam("latitud") @DefaultValue("100") double latitud, 
            @QueryParam("longitud") @DefaultValue("100") double longitud,
            @QueryParam("plato") @DefaultValue("") String plato
            ) throws Exception {
	
		List<Specification<Restaurante>> especificaciones = new ArrayList<>();
		if(radio != -1)
			especificaciones.add(new IsRadioSpecification(latitud, longitud, radio));
		if(!plato.isBlank())
			especificaciones.add(new IsContienePlatoSpecification(plato));
		
		if(especificaciones.size() == 0)
			return getListadoRestaurantes(servicio.getListadoRestaurantes());
		Specification<Restaurante> esp = new AndSpecification<Restaurante>(especificaciones);
		return getListadoRestaurantes(servicio.getListadoRestaurantesBySpecification(esp));
	}	
	
	// addPlato
	@PUT
	@Path("/{restaurante}/plato/add")
	@Consumes(MediaType.APPLICATION_XML)
	public Response addPlato(@PathParam("restaurante") String id, Plato plato) throws Exception {
		
		if(!servicio.contienePlato(id, plato.getNombre())) {
			// añadir
			servicio.addPlato(id, plato);
		}
		else{
			throw new IllegalArgumentException("El restaurante: " + id + " ya contiene el plato: " + plato.getNombre());	
		}
		
		return Response.status(Response.Status.NO_CONTENT).build();		
	}
	
	// updatePlato
	@PUT
	@Path("/{restaurante}/plato/update")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updatePlato(@PathParam("restaurante") String id, Plato plato) throws Exception {
		
		if(!servicio.contienePlato(id, plato.getNombre()))
			throw new EntidadNoEncontradaException("El restaurante: " + id + " no contiene el plato: " + plato.getNombre());			
		
		servicio.updatePlato(id, plato);
		
		return Response.status(Response.Status.NO_CONTENT).build();		
	}
	
	// removePlato
	@DELETE
	@Path("/{restaurante}/plato")
	public Response removePlato(@PathParam("restaurante") String id, @QueryParam("nombre") String nombre) throws Exception {
		
		if(!servicio.contienePlato(id, nombre))
			throw new EntidadNoEncontradaException("El restaurante: " + id + " no contiene el plato: " + nombre);			
		
		
		servicio.removePlato(id, nombre);

		return Response.status(Response.Status.NO_CONTENT).build();
	}
}

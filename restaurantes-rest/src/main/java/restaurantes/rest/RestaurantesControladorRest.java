package restaurantes.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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
import restaurantes.rest.ListadoSitioTuristico.SitioTuristicoResumen;

@Api
@Path("restaurantes")
public class RestaurantesControladorRest {

	private IServicioRestaurantes servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class);

	@Context
	private UriInfo uriInfo;
	
	// curl -i http://localhost:8080/api/restaurantes/1
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta un restaurante", notes = "Retorna un restaurante utilizando su id", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
							@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "El restaurante id no existe"),
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El id esta vacio") })
	public Response getRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws Exception {

		return Response.status(Response.Status.OK).entity(servicio.getRestaurante(id)).build();
	}

	// curl -i -X POST -H "Content-Type: application/json" -d '{"nombre":"Restaurante Prueba","latitud" : 0, "longitud" : 0}' http://localhost:8080/api/restaurantes

	@POST
	@ApiOperation(value="Creacion de un restaurante", notes="Retorna la nueva url del restaurante", response = URI.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = ""),
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El restaurante es nulo."),
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El restaurante no tiene nombre."),
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El restaurante ya existe.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Restaurante restaurante) throws Exception {

		String id = servicio.create(restaurante);

		URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(nuevaURL).build();
	}

	// curl -i -X PUT -H "Content-Type: application/json" -d '{"id" : 1, "nombre":"Restaurante Actualizado","latitud" : 800, "longitud" : -200}' http://localhost:8080/api/restaurantes/1

	@PUT
	@Path("/{id}")
	@ApiOperation(value="Actualizar un restaurante", notes="No retorna nada", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
							@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El identificador no coincide"), 
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El restaurante es nulo."), 
							@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El restaurante no tiene nombre."),
							@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "El id:id no es valido"),
							@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el restaurante con id : id")
							})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Restaurante restaurante) throws Exception {

		if (!id.equals(restaurante.getId()))
			throw new IllegalArgumentException("El identificador no coincide: " + id);

		servicio.update(restaurante);

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// curl -i -X DELETE http://localhost:8080/api/restaurantes/1

	@DELETE
	@Path("/{id}")
	@ApiOperation(value="Elimina un restaurante", notes="No retorna nada")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
							@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El id no es valido"),
							@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No existe la entidad"), 
							
							})
	public Response removeRestaurante(@PathParam("id") String id) throws Exception {

		servicio.removeRestaurante(id);

		return Response.status(Response.Status.NO_CONTENT).build();

	}
	
	
	//curl -i -H "Accept: application/json" -X GET http://localhost:8080/api/restaurantes/1/sitiosProximos
	
	@GET
	@Path("/{id}/sitiosProximos")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Obtiene la lista de sitios turísticos próximos al restaurante", notes = "Retorna los sitios próximos de un restaurante utilizando su id", response = SitioTuristico.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response getSitiosProximosRestaurante(
			@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws Exception {

		ListadoSitioTuristico l = new ListadoSitioTuristico();
		
		List<SitioTuristicoResumen> lista = new ArrayList<SitioTuristicoResumen>();
		
		for(SitioTuristico sitio : servicio.getSitiosTuristicosProximos(id)) {
			SitioTuristicoResumen nuevo = l.new SitioTuristicoResumen(sitio.getNombre(), sitio.getDescripcion(), sitio.getResumen(), sitio.getImagen());
			lista.add(nuevo);
		}
		
		l.setLista(lista);
		return Response.ok(l,MediaType.APPLICATION_JSON).build();
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
	
	// curl -i -X GET -H "Accept: application/json" http://localhost:8080/api/restaurantes
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Obtiene el listado de los restaurantes", notes = "Obtiene el listado de los restaurantes según su radio, latitud, longitud y/o platos contenidos", response = List.class	)
	@ApiResponses(value =
			@ApiResponse(code = HttpServletResponse.SC_OK, message = ""))
	public Response getListadoRestaurantes(
			@ApiParam(value = "radio de búsqueda", required = false) @QueryParam("radio") @DefaultValue("-1") int radio, 
			@ApiParam(value = "latitud desde la que iniciar la búsqueda", required = false) @QueryParam("latitud") @DefaultValue("100") double latitud, 
			@ApiParam(value = "longitud desde la que iniciar la búsqueda", required = false) @QueryParam("longitud") @DefaultValue("100") double longitud,
			@ApiParam(value = "nombre del plato por el que filtrar", required = false) @QueryParam("plato") @DefaultValue("") String plato
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


	// curl -i -X POST -H "Content-Type: application/json" -d '{"nombre":"Manzana","descripcion":"Muy roja","precio":2.0}' http://localhost:8080/api/restaurantes/1/plato
	
	@POST
	@Path("/{restaurante}/plato")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Añade un plato", notes = "Añade un plato de un restaurante según su id"	)
	@ApiResponses(value = { 
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El restaurante ya contiene el plato"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "El id del restaurante está vacío"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El plato es nulo")})
	public Response addPlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("restaurante") String id, 
			@ApiParam(value = "plato que se desea añadir", required = true) Plato plato) throws Exception {
		
		if(!servicio.contienePlato(id, plato.getNombre())) {
			// añadir
			servicio.addPlato(id, plato);
		}
		else{
			throw new IllegalArgumentException("El restaurante: " + id + " ya contiene el plato: " + plato.getNombre());	
		}
		
		return Response.status(Response.Status.NO_CONTENT).build();		
	}
	
	// curl -i -X PUT -H "Content-Type: application/json" -d '{"nombre":"Manzana","precio":10.99}' http://localhost:8080/api/restaurantes/1/plato
	
	@PUT
	@Path("/{restaurante}/plato")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Actualiza un plato", notes = "Actualiza un plato de un restaurante según su id")
	@ApiResponses(value = { 
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "El restaurante no contiene el plato"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No existe ningún restaurante con ese id"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "El plato no puede ser nulo")})
	public Response updatePlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("restaurante") String id, 
			@ApiParam(value = "plato a actualizar", required = true) Plato plato) throws Exception {
		
		if(!servicio.contienePlato(id, plato.getNombre()))
			throw new EntidadNoEncontradaException("El restaurante: " + id + " no contiene el plato: " + plato.getNombre());			
		
		servicio.updatePlato(id, plato);
		
		return Response.status(Response.Status.NO_CONTENT).build();		
	}
	
	// curl -i -X DELETE -d 'nombre=Manzana' http://localhost:8080/api/restaurantes/1/plato
	
	@DELETE
	@Path("/{restaurante}/plato")
	@ApiOperation(value = "Elimina un plato", notes = "Elimina un plato de un restaurante según su id"	)
	@ApiResponses(value = { 
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "El restaurante no contiene el plato"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No existe ningún restaurante con ese id")})
	public Response removePlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("restaurante") String id, 
			@ApiParam(value = "nombre del plato", required = true) @FormParam("nombre") String nombre) throws Exception {
		
		if(!servicio.contienePlato(id, nombre))
			throw new EntidadNoEncontradaException("El restaurante: " + id + " no contiene el plato: " + nombre);			
		
		
		servicio.removePlato(id, nombre);

		return Response.status(Response.Status.NO_CONTENT).build();
	}
}

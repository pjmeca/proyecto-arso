package restaurantes.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import arso.repositorio.EntidadNoEncontradaException;

@Provider
public class TratamientoEntidadNoEncontradaException implements ExceptionMapper<EntidadNoEncontradaException> {
	
	@Override
	public Response toResponse(EntidadNoEncontradaException arg0) {
		
		return Response.status(Response.Status.NOT_FOUND).entity(arg0.getMessage()).build();
	}
}
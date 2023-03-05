package arso.repositorio;

/*
 * Excepción notificada si no existe el identificador de la entidad.
 */

@SuppressWarnings("serial")
public class EntidadNoEncontradaException extends Exception {

	public EntidadNoEncontradaException(String msg, Throwable causa) {		
		super(msg, causa);
	}
	
	public EntidadNoEncontradaException(String msg) {
		super(msg);		
	}
	
		
}

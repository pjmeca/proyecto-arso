package arso.opiniones.modelo;

import java.time.LocalDateTime;

public class Valoracion {

	private String correo;
	private LocalDateTime fecha;
	private double calificacion;
	private String comentario;
	
	public Valoracion() {
		fecha = LocalDateTime.now();
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(double calificacion) {
		if(calificacion < 1 || calificacion > 5)
			throw new IllegalArgumentException("La calificación debe encontrarse entre 1 y 5.");
		
		this.calificacion = calificacion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	
}

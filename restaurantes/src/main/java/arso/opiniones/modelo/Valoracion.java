package arso.opiniones.modelo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import arso.utils.LocalDateTimeDeserializer;

public class Valoracion {

	@JsonProperty("Correo")
	private String correo;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonProperty("Fecha")
	private LocalDateTime fecha;
	
	@JsonProperty("Calificacion")
	private float calificacion;
	
	@JsonProperty("Comentario")
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

	public float getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(float calificacion) {
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

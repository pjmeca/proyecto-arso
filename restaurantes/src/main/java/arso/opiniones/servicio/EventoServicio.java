package arso.opiniones.servicio;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import arso.opiniones.modelo.Valoracion;
import arso.repositorio.EntidadNoEncontradaException;
import arso.repositorio.RepositorioException;
import arso.restaurantes.servicio.IServicioRestaurantes;

public class EventoServicio {

	public static final String URI = System.getenv("RABBITMQ_URI");
	public static final String EXCHANGE = System.getenv("RABBITMQ_EXCHANGE");
	public static final String QUEUE = System.getenv("RABBITMQ_QUEUE");
	public static final String ROUTING_KEY = System.getenv("RABBITMQ_ROUTING_KEY");

	public static void suscribirse(IServicioRestaurantes servicio) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(URI);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true);

		channel.queueDeclare(QUEUE, true, false, false, null);
		channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		channel.basicConsume(QUEUE, false, "arso-consumidor", new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				String routingKey = envelope.getRoutingKey();
				String contentType = properties.getContentType();
				long deliveryTag = envelope.getDeliveryTag();
				
				String contenido = new String(body, "UTF-8");
				System.out.println(contenido);
				
				ObjectMapper objectMapper = new ObjectMapper();
				Mensaje mensaje = objectMapper.readValue(contenido, Mensaje.class);
						
				System.out.println("Recibido mensaje: " + mensaje);
				
				// Buscar si el restaurante está en el servicio
				try {
					servicio.updateResumenOpinion(mensaje.getIdOpinion(), mensaje.getNValoraciones(), mensaje.getCalMedia());
				} catch (RepositorioException | EntidadNoEncontradaException e) {
					throw new IOException();
				}
				
				// Confirma el procesamiento
				channel.basicAck(deliveryTag, false);
			}
		});
	}
}

class Mensaje{ 
	@JsonProperty("IdOpinion")
    private String idOpinion;
	@JsonProperty("Valoracion")
	private Valoracion valoracion;
	@JsonProperty("NumValoraciones")
	private int nValoraciones;
	@JsonProperty("CalMedia")
	private float calMedia;
    
    public Mensaje() {
    	
    }
    
	public String getIdOpinion() {
		return idOpinion;
	}
	public void setIdOpinion(String idOpinion) {
		this.idOpinion = idOpinion;
	}
	public Valoracion getValoracion() {
		return valoracion;
	}
	public void setValoracion(Valoracion valoracion) {
		this.valoracion = valoracion;
	}
	public int getNValoraciones() {
		return this.nValoraciones;
	}
	public void setNValoraciones(int nValoraciones) {
		this.nValoraciones = nValoraciones;
	}
	public float getCalMedia() {
		return this.calMedia;
	}
	public void setCalMedia(float calMedia) {
		this.calMedia = calMedia;
	}
}
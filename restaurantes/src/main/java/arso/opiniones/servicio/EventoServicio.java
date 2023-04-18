package arso.opiniones.servicio;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import arso.opiniones.modelo.Valoracion;

public class EventoServicio {

	public static final String URI = "amqps://dbuadusv:RUItokUvjf65IFZDxkDZ6J1Z3punicMq@rat.rmq2.cloudamqp.com/dbuadusv";
	public static final String EXCHANGE = "amq.direct";
	public static final String QUEUE = "arso";
	public static final String ROUTING_KEY = "arso";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(URI);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE, true, false, false, null);
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
				
//				JsonReader js = Json.createReader(new StringReader(contenido));
//				Mensaje mensaje = js.readObject();
//				js.close();
						
				System.out.println(mensaje);
				
				// Confirma el procesamiento
				channel.basicAck(deliveryTag, false);
			}
		});
	}
}

class Mensaje{
    public String idOpinion;
    public Valoracion valoracion;
    public int numValoraciones;
    public float calMedia;
    
    public Mensaje() {
    	
    }
    
	public String getIdOpinion() {
		return idOpinion;
	}
	public void setIdOpinion(String idOpinion) {
		idOpinion = idOpinion;
	}
	public Valoracion getValoracion() {
		return valoracion;
	}
	public void setValoracion(Valoracion valoracion) {
		valoracion = valoracion;
	}
	public int getNumValoraciones() {
		return numValoraciones;
	}
	public void setNumValoraciones(int numValoraciones) {
		numValoraciones = numValoraciones;
	}
	public float getCalMedia() {
		return calMedia;
	}
	public void setCalMedia(float calMedia) {
		calMedia = calMedia;
	}
}
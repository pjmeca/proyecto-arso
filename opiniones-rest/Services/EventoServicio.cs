using System.IO;
using System.Text;
using RabbitMQ.Client;
using Opiniones.Modelo;
using Newtonsoft.Json;

namespace Eventos;

public class Mensaje{
    public string IdOpinion{get;set;}
    public Valoracion Valoracion{get;set;}
    public int NumValoraciones{get;set;}
    public float CalMedia{get;set;}

}

public class EventoServicio 
{
    public static string URI = Environment.GetEnvironmentVariable("RABBITMQ_URI");
    public static string EXCHANGE = Environment.GetEnvironmentVariable("RABBITMQ_EXCHANGE");
    public static string QUEUE = Environment.GetEnvironmentVariable("RABBITMQ_QUEUE");
    public static string ROUTING_KEY = Environment.GetEnvironmentVariable("RABBITMQ_ROUTING_KEY");

    public static void send(Mensaje mensaje)
    {
        var factory = new ConnectionFactory { Uri = new Uri(URI) };
        using var connection = factory.CreateConnection();
        using var channel = connection.CreateModel();

        channel.QueueDeclare(queue: QUEUE,
                            durable: true,
                            exclusive: false,
                            autoDelete: false,
                            arguments: null);

        var json = JsonConvert.SerializeObject(mensaje);
        var body = Encoding.UTF8.GetBytes(json);

        channel.ExchangeDeclare(exchange: EXCHANGE,
                                type: ExchangeType.Direct,
                                durable: true,
                                autoDelete: false);

        channel.BasicPublish(exchange: EXCHANGE,
                            routingKey: ROUTING_KEY,
                            basicProperties: null,
                            body: body);
        Console.WriteLine($" [x] Sent {mensaje}");
    }
}
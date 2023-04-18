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

class EventoServicio 
{
    public const string URI = "amqps://dbuadusv:RUItokUvjf65IFZDxkDZ6J1Z3punicMq@rat.rmq2.cloudamqp.com/dbuadusv";
    public const string EXCHANGE = "amq.direct";
    public const string QUEUE = "arso";
    public const string ROUTING_KEY = "arso";

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

        channel.BasicPublish(exchange: EXCHANGE,
                            routingKey: ROUTING_KEY,
                            basicProperties: null,
                            body: body);
        Console.WriteLine($" [x] Sent {mensaje}");
    }
}
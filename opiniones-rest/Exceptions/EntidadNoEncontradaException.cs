
namespace Opiniones.Exceptions;

public class EntidadNoEncontrada : Exception
{
    public EntidadNoEncontrada() : base("Entidad no encontrada")
    {
    }

    public EntidadNoEncontrada(string message)
        : base(message)
    {
    }
}
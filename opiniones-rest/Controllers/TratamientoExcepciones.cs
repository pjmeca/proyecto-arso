using System.Net.Http;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Opiniones.Exceptions;

[ApiController]
[Route("/error")]
public class ErrorController : Controller
{
    [HttpGet]
    [HttpPut]
    [HttpPost]
    [HttpDelete]
    public IActionResult HandleError()
    {
        var exception = HttpContext.Features.Get<IExceptionHandlerFeature>()?.Error;
        
        if (exception is ArgumentException)
        {
            return BadRequest(exception.Message);
        }
        else if (exception is EntidadNoEncontrada)
        {
            return NotFound();
        }
        else
        {
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }
}

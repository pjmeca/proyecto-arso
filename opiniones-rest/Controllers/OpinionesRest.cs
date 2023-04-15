
using Opiniones.Modelo;
using Opiniones.Servicio;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using Opiniones.Exceptions;

namespace Opiniones.Controllers;

[Route("api/opiniones")]
[ApiController]
public class OpinionesController : ControllerBase
{
    private readonly IServicioOpiniones _servicio;

    public OpinionesController(IServicioOpiniones servicio)
    {
        _servicio = servicio;
    }

    [HttpGet("", Name = "GetOpiniones")]
    public ActionResult<List<Opinion>> GetAll()
    {
        var opiniones = _servicio.GetAll();

        return opiniones;
    }

    [HttpGet("{id}", Name = "GetOpinion")]
    public ActionResult<Opinion> Get(string id)
    {
        return _servicio.Get(id);
    }

    [HttpPost("{nombre}")]
    public ActionResult<string> Create(string nombre)
    {
        string nuevoId = _servicio.Create(nombre);
        Console.WriteLine(nuevoId);

        return Ok(nuevoId);
    }

    [HttpDelete("{id}")]
    public IActionResult Delete(string id)
    {
        var opinion = _servicio.Get(id);

        _servicio.Delete(opinion);

        return NoContent();
    }

    [HttpPut("{id}")]
    public IActionResult AddValoracion(string id, Valoracion valoracion)
    {
        var opinion = _servicio.Get(id);

        _servicio.AddValoracion(id,valoracion);

        return NoContent();
    }

    [HttpGet("{id}/valoraciones", Name = "GetValoraciones")]
    public ActionResult<List<Valoracion>> GetValoraciones(string id) 
    {
        return _servicio.GetValoraciones(id);
    }
}
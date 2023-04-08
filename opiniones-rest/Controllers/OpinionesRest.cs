
using Opiniones.Modelo;
using Opiniones.Servicio;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

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
        var entidad = _servicio.Get(id);

        if (entidad == null)
        {
            return NotFound();
        }

        return entidad;
    }

    [HttpPost]
    public ActionResult<Opinion> Create([FromForm] string nombre)
    {
        string nuevoId = _servicio.Create(nombre);

        return CreatedAtRoute("GetOpinion", new { id = nuevoId }, "");
    }

    [HttpDelete("{id}")]
    public IActionResult Delete(string id)
    {
        var opinion = _servicio.Get(id);

        if (opinion == null)
        {
            return NotFound();
        }

        _servicio.Delete(opinion);

        return NoContent();
    }
}
using Opiniones.Modelo;
using Opiniones.Repositorio;
using Opiniones.Servicio;
using Repositorio;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddSingleton<Repositorio<Opinion, String>, RepositorioOpinionesMongoDB>(); 
builder.Services.AddSingleton<IServicioOpiniones, ServicioOpiniones>();

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

//app.UseHttpsRedirection();

app.UseExceptionHandler("/error"); // par manejar las excepciones globalmente

app.UseAuthorization();

app.MapControllers();

app.Run("http://opiniones-rest:5041");

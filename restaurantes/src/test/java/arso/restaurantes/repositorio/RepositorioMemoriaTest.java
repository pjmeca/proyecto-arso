package arso.restaurantes.repositorio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import arso.especificacion.IsRadioSpecification;
import arso.repositorio.RepositorioException;
import arso.restaurantes.modelo.Restaurante;

public class RepositorioMemoriaTest {

	private RepositorioRestaurantesMemoria repositorio;
	
	@BeforeEach
	public void init() throws RepositorioException {
		repositorio = new RepositorioRestaurantesMemoria();
		repositorio.add(new Restaurante("Burger", 1000,1000));
		repositorio.add(new Restaurante("Comic", 1100,1100));
		repositorio.add(new Restaurante("McDonalds", 900,900));
		repositorio.add(new Restaurante("Sillon", 10,1000));	
	}
	
	@Test
	public void getBySpecificationTest() {
		
		List<Restaurante> lista = repositorio.getBySpecification(new IsRadioSpecification(1000.0,1000.0,100.0));
		assertTrue(lista.stream().filter(r -> r.getNombre().equals("Burger")).count() > 0);
		assertTrue(lista.stream().filter(r -> r.getNombre().equals("Comic")).count() > 0);
		assertTrue(lista.stream().filter(r -> r.getNombre().equals("McDonalds")).count() > 0);
	
		for (Restaurante r : lista){
			System.out.println("Restaurante "+r.getNombre()+" esta dentro del radio");
		}	
	}
}

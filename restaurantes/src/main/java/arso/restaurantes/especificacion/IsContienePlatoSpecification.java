package arso.restaurantes.especificacion;

import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;

import arso.especificacion.Specification;
import arso.restaurantes.modelo.Restaurante;

public class IsContienePlatoSpecification  implements Specification<Restaurante> {
	
	private String nombre;
	
	public IsContienePlatoSpecification(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public boolean isSatisfied(Restaurante restaurante) {
		return restaurante.getPlato(nombre) != null;
	}
	
	@Override
	public Bson toBsonFilter() {
        
		return Filters.elemMatch("platos",Filters.eq("nombre",nombre));		
	}
}
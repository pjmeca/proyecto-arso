package arso.restaurantes.especificacion;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.model.Filters;

import arso.especificacion.Specification;
import arso.restaurantes.modelo.Restaurante;

public class IsOpinionSpecification  implements Specification<Restaurante> {
	
	private String idOpinion;
	
	public IsOpinionSpecification(String idOpinion) {
		this.idOpinion = idOpinion;
	}

	@Override
	public boolean isSatisfied(Restaurante restaurante) {
		String id = restaurante.getOpinion();
		return id != null && id.equals(idOpinion);
	}
	
	@Override
	public Bson toBsonFilter() {
        
		return Filters.eq("opinion",new ObjectId(idOpinion));		
	}
}
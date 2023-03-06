package arso.especificacion;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
public class NotSpecification<T> implements Specification<T> {

    private Specification<T> specification;

    public NotSpecification(Specification<T> specification) {
        this.specification = specification;
    }

    public boolean isSatisfied(T object) {
        return !specification.isSatisfied(object);
    }
    
    @Override
	public Bson toBsonFilter() {
		return Filters.not(specification.toBsonFilter());
	}
}
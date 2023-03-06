package arso.especificacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

public class AndSpecification<T> implements Specification<T> {

    private List<Specification<T>> specifications;

    public AndSpecification(Specification<T>... specifications) {
        this.specifications = Arrays.asList(specifications);
    }

    public boolean isSatisfied(T object) {
        return specifications.stream().allMatch(s -> { return s.isSatisfied(object); });
    }

	@Override
	public Bson toBsonFilter() {
        ArrayList<Bson> lista = new ArrayList<Bson>();
        for(Specification<T> s : specifications){
            lista.add(s.toBsonFilter());
        }
		return Filters.and(lista);
	}
}
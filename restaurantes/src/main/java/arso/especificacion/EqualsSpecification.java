package arso.especificacion;

import java.beans.PropertyDescriptor;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class EqualsSpecification<T> implements Specification<T>{

    private String property;
    private Object value;
    private Class<T> clase;

    public EqualsSpecification(String property, Object value, Class<T> clase) {
        this.property = property;
        this.value = value;
        this.clase = clase;
    }

    @Override
    public boolean isSatisfied(T product) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(property, clase);
            Object v = descriptor.getReadMethod().invoke(product);
            if (v == value) {
                return true;
            }
            if (v != null && value == null || v == null && value != null) {
                return false;
            }
            return value.equals(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
	public Bson toBsonFilter() {
        return Filters.eq(property, value);
	}
}
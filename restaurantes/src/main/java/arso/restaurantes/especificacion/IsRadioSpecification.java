package arso.restaurantes.especificacion;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import arso.especificacion.Specification;
import arso.restaurantes.modelo.Restaurante;

public class IsRadioSpecification implements Specification<Restaurante> {
	
	private double latitud, longitud, radio;
	
	public IsRadioSpecification(double latitud, double longitud, double radio) {
		this.latitud = latitud;
		this.longitud = longitud;
		this.radio = radio;
	}

	@Override
	public boolean isSatisfied(Restaurante object) {
		boolean latitudSatisfied = latitud-radio <= object.getLatitud() 
				&& object.getLatitud() <= latitud+radio; 
		boolean longitudSatisfied = longitud-radio <= object.getLongitud() 
				&& object.getLongitud() <= longitud+radio; 
		
		return latitudSatisfied && longitudSatisfied;
	}
	
	
	 @Override
	public Bson toBsonFilter() {
		Bson lat1 = Filters.gte("latitud", latitud-radio);
		Bson lat2 = Filters.lte("latitud", latitud+radio);
		Bson lon1 = Filters.gte("longitud", longitud-radio);
		Bson lon2 = Filters.lte("longitud", longitud+radio);
		return Filters.and(lat1,lat2,lon1,lon2);
	}
}

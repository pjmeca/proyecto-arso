package arso.especificacion;

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
	
	

}

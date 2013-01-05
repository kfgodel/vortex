/**
 * 
 */
package net.gaia.vortex.router.impl.sets.converter;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;

/**
 * Esta interfaz define el comportamiento necesario para convertir condiciones en mapas serializables y viceversa
 * @author kfgodel
 */
public interface ConversorDeCondiciones {

	/**
	 * Convierte la condicion pasada en un mapa serializable como mensaje json
	 * @param condicion La condición a convertir
	 * @return El mapa que representa la condicion
	 */
	public Map<String, Object> convertirAMapa(Condicion condicion);
	
	/**
	 * Convierte el mapa recibido en una condición que es utilizable para evaluar mapas
	 * @param mapa El mapa serializado que representa una condición
	 * @return La condición generada a partir del mapa
	 */
	public Condicion convertirDesdeMapa(Map<String, Object> mapa);
	
}

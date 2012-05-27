/**
 * 26/05/2012 10:50:28 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.core.api.metrics;

/**
 * Esta interfaz define los valores disponibles como métricas del nodo en una cantidad de tiempo
 * 
 * @author D. García
 */
public interface MetricasPorTiempo {
	/**
	 * Devuelve la cantidad de mensajes que fueron recibidos por este nodo para ser ruteados
	 * 
	 * @return La cantidad de mensajes recibidos desde el inicio de esta métrica hasta el final
	 */
	public long getCantidadDeMensajesRecibidos();

	/**
	 * Devuelve la cantidad de mensajes que este nodo entregó a los nodos vecinos correspondientes.<br>
	 * Esta medida puede ser un aproximado muy cercano, variando en pequeñas cantidades por la
	 * manera en que se mide
	 * 
	 * @return La cantidad de mensajes que fueron ruteados desde el inicio de esta metrica hasta el
	 *         final
	 */
	public long getCantidadDeMensajesRuteados();

	/**
	 * Devuelve la cantidad de milisegundos que abarca el período de este métrica.<br>
	 * Es la medición en ms de cuanto tiempo se estuvo registrando actividad para esta métrica
	 * 
	 * @return El tiempo medido en milisegundos desde su creación
	 */
	public long getDuracionDeMedicionEnMilis();

	/**
	 * Devuelve el momento en que se comenzó la medición como timestamp tomado del sistema en milis
	 * 
	 * @return El momento de referencia de esta medición
	 */
	public long getMomentoDeInicioDeLaMedicionEnMilis();

	/**
	 * Devuelve la tasa entre la cantidad de mensajes enviados / la cantidad de mensajes recibidos
	 * durante el período de esta métrica. Indicando que porcentaje de lo recibido fue entregado. Si
	 * el nodo no está saturado este valor debería ser lo más cercano a 1.0 posible
	 * 
	 * @return El valor <= 1.0 que indica cuanto de lo que se recibió fue enviado
	 */
	public double getTasaDeDelivery();

	/**
	 * Devuelve la cantidad de mensajes recibidos por milisegundo en el período contemplado por esta
	 * métrica
	 * 
	 * @return La velocidad con la que los mensajes fueron aceptados por este nodo en el período
	 *         medido
	 */
	public double getVelocidadDeRecepcion();

	/**
	 * Devuelve la cantidad de mensajes enviados a nodos vecinos por milisegundo en el período
	 * contemplado por esta métrica
	 * 
	 * @return La velocidad con la que los mensajes fueron enviados a los nodos vecinos
	 *         correspondientes durante el período medido
	 */
	public double getVelocidadDeEnvio();

	/**
	 * Devuelve el momento de fin de la medición de esta métrica
	 * 
	 * @return El momento de inicio + la duración de la medición
	 */
	public long getMomentoDeFinDeLaMedicion();

	/**
	 * Devuelve la edad de esta medición en milisegundos transcurridos desde el momento de fin hasta
	 * el momento actual.<br>
	 * Esta cantidad puede ser negativa si está en progreso y varía de invocación a invocación con
	 * el paso del tiempo
	 * 
	 * @return La edad en milis desde el momento de fin
	 */
	public long getEdadEnMilis();
}

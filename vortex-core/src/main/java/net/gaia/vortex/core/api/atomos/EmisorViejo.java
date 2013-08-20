/**
 * 14/06/2012 20:01:11 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api.atomos;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.ComponenteVortex;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.atomos.forward.Nexo;

/**
 * Esta interfaz representa un componente de la red vortex que tiene la capacidad de enviar mensajes
 * a receptores a los que esta conectado.<br>
 * La implementación de esta interfaz por parte de una clase implica que el componente puede mandar
 * mensajes a otros, pero no implica que pueda recibirlos. El componente permite realizar conexiones
 * a otros componentes a los cuales puede enviarles mensajes y así formar una topología de red.<br>
 * <br>
 * Las implementaciones de esta interfaz pueden estar limitadas a 1 o N receptores. Depende de la
 * implementación concreta qué pasa cuando se conecta estando ya conectado. Se puede perder la
 * conexión previa ({@link Nexo}), o se pueden agregar nuevas ({@link Multiplexor})
 * 
 * @author D. García
 */
@Deprecated
public interface EmisorViejo extends ComponenteVortex {

	/**
	 * Realiza una conexión desde este componente al componente destino de manera que el destino
	 * reciba los mensajes emitidos desde este componente.<br>
	 * La implementación de esta interfaz puede agregar el destino a un conjunto, o reemplazar al
	 * único destino que conoce
	 * 
	 * @param destino
	 *            El receptor que será agregado o que reemplazará al previo (dependiendo de la
	 *            Implementación)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Realiza una desconexión del receptor pasado. Si no estaba previamente conectado al receptor
	 * esta acción no tiene consecuencias.<br>
	 * Si al quitar el destino indicado este emisor se queda sin destinos, dependerá de la
	 * implementación el comportamiento esperado. Normalmente un emisor sin destino descarta los
	 * mensajes que tiene para enviar
	 * 
	 * @param destino
	 *            El destino a quitar
	 */
	public void desconectarDe(Receptor destino);

}

/**
 * 16/06/2012 19:03:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.mensaje;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import ar.com.dgarcia.colecciones.sets.ConcurrentHashSet;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeConContenido implements MensajeVortex {

	/**
	 * Key utilizada para guardar el registro de moleculas visitadas
	 */
	public static final String TRAZA_IDENTIFICADORES_VORTEX_KEY = "traza_identificadores";
	/**
	 * Clave usada para agregar como dato el nombre completo de la clase a partir de la cual se
	 * origina este mensaje
	 */
	public static final String CLASSNAME_KEY = "CLASSNAME_KEY";

	private ContenidoVortex contenido;
	public static final String contenido_FIELD = "contenido";

	private Set<String> idsVisitados;
	public static final String idsVisitados_FIELD = "idsVisitados";

	private int contador = 0;

	public Set<String> getIdsVisitados() {
		return idsVisitados;
	}

	/**
	 * Creamos el set de los nodos visitados a partir de los datos pasados como ids visitados.
	 * Reemplazamos la entrada previa en el mapa con el set creado por esta instancia
	 * 
	 * @param moleculasVisitadasSegunMapa
	 *            El conjunto de IDs visitados según estado previo
	 * @return El conjunto de nodos visitados inicialmente
	 */
	private void inicializarIdsVisitados(final Collection<String> moleculasVisitadasSegunMapa) {
		this.idsVisitados = new ConcurrentHashSet<String>();
		idsVisitados.addAll(moleculasVisitadasSegunMapa);
		getContenido().put(TRAZA_IDENTIFICADORES_VORTEX_KEY, idsVisitados);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getContenido()
	 */
	@Override
	public ContenidoVortex getContenido() {
		return contenido;
	}

	/**
	 * Crea un mensaje vacío sin registro de nodos visitados
	 * 
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeConContenido create() {
		final ContenidoVortex contenidoVacio = ContenidoMapa.create();
		return create(contenidoVacio, Collections.EMPTY_SET);
	}

	/**
	 * Crea un nuevo mensaje vortex con el estado pasado, pero sin registro de nodos visitados
	 * previamente
	 * 
	 * @param contenido
	 *            El contenido del mensaje
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeConContenido create(final ContenidoVortex contenido) {
		return create(contenido, Collections.EMPTY_SET);
	}

	/**
	 * Crea un mensaje vortex con el contenido indicado y el registro de nodos visitados pasado
	 * 
	 * @param contenidoIncial
	 *            El contenido a portar por este mensaje
	 * @param idsVisitados
	 *            Los ids tomados como visitados
	 * @return El mensaje creado
	 */
	public static MensajeConContenido create(final ContenidoVortex contenido, final Collection<String> idsVisitados) {
		final MensajeConContenido mensaje = new MensajeConContenido();
		mensaje.contenido = contenido;
		mensaje.inicializarIdsVisitados(idsVisitados);
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(contenido_FIELD, contenido).add(idsVisitados_FIELD, idsVisitados).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getValorComoPrimitiva()
	 */
	@Override
	public Object getValorComoPrimitiva() {
		return getContenido().get(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	@Override
	public void setValorComoPrimitiva(final Object valor) {
		if (!ContenidoPrimitiva.esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede ser aceptado como primitiva vortex");
		}
		getContenido().put(ContenidoVortex.PRIMITIVA_VORTEX_KEY, valor);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#tieneValorComoPrimitiva()
	 */
	@Override
	public boolean tieneValorComoPrimitiva() {
		return getContenido().containsKey(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	@Override
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		getContenido().put(CLASSNAME_KEY, nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getNombreDelTipoOriginal()
	 */
	@Override
	public String getNombreDelTipoOriginal() {
		return (String) getContenido().get(CLASSNAME_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#pasoPreviamentePor(net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex)
	 */
	@Override
	public boolean pasoPreviamentePor(final IdentificadorVortex identificador) {
		return contador > 6;
		// final String valorDelIdentificador = identificador.getValorActual();
		// final boolean yaTenemosRegistroDelIdentificador =
		// getIdsVisitados().contains(valorDelIdentificador);
		// return yaTenemosRegistroDelIdentificador;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#registrarPasajePor(net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex)
	 */
	@Override
	public void registrarPasajePor(final IdentificadorVortex identificador) {
		contador++;
		// final String valorDelIdentificador = identificador.getValorActual();
		// getIdsVisitados().add(valorDelIdentificador);
	}
}

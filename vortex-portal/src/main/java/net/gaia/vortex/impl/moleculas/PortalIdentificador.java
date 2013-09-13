/**
 * Created on: Sep 7, 2013 8:27:52 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.moleculas;

import java.util.List;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Identificador;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.api.moleculas.portal.ErrorDeMapeoVortexException;
import net.gaia.vortex.api.moleculas.portal.HandlerDePortal;
import net.gaia.vortex.impl.support.EmisorSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el portal que además de convertir los objetos, identifica los mensajes para
 * no recibir duplicados
 * 
 * @author dgarcia
 */
public class PortalIdentificador extends EmisorSupport implements Portal {

	private Portal conversor;
	public static final String conversor_FIELD = "conversor";

	private Identificador identificador;
	public static final String identificador_FIELD = "identificador";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Los mensajes externos los filtra el identificador
		this.identificador.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		this.conversor.enviar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#recibirCon(net.gaia.vortex.api.moleculas.portal.HandlerDePortal)
	 */
	public <T> void recibirCon(final HandlerDePortal<T> handlerDeMensajes) {
		this.conversor.recibirCon(handlerDeMensajes);
	}

	public static PortalIdentificador create(final VortexPortal builder) {
		final PortalIdentificador portal = new PortalIdentificador();
		portal.inicializarCon(builder);
		return portal;
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	private void inicializarCon(final VortexPortal builder) {
		// Creamos el portal que sabe convertir los objetos
		this.conversor = builder.portalConversor();
		// Creamos el identificador que filtra los mensajes
		this.identificador = builder.getCore().identificador();

		// El conversor enviara los mensajes a traves del identificador para asignar ID
		this.conversor.conectarCon(this.identificador.getConectorParaEnviarConId());

		// El identificador actuara como filtro para que reciba el conversor
		this.identificador.getConectorParaRecibirSinDuplicados().conectarCon(this.conversor);
	}

	/**
	 * @see net.gaia.vortex.impl.support.ComponenteSupport#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(conversor_FIELD, conversor)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		identificador.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar() {
		identificador.desconectar();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		identificador.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados() {
		return identificador.getConectados();
	}

}

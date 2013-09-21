/**
 * Created on: Sep 7, 2013 7:34:31 PM by: Dario L. Garcia
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

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Identificador;
import net.gaia.vortex.impl.condiciones.EsMensajeNuevoDeOtroComponente;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaDeMensajes;
import net.gaia.vortex.impl.support.EmisorSupport;
import net.gaia.vortex.impl.transformaciones.GenerarIdEnMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la molécula que permite identificar los mensajes enviados y recibidos,
 * evitando duplicados.<br>
 * Se crea un filtro y un transformador utilizado para los mensajes entrante, y salientes
 * respectivamente
 * 
 * @author dgarcia
 */
public class MoleculaIdentificador extends EmisorSupport implements Identificador {

	private IdDeComponenteVortex idPropio;
	public static final String idPropio_FIELD = "idPropio";

	private Filtro filtroRecibidos;
	private Transformador transformadorEnviados;

	public static MoleculaIdentificador create(final VortexCore builder) {
		final MoleculaIdentificador identificador = new MoleculaIdentificador();
		identificador.inicializarCon(builder);
		return identificador;
	}

	/**
	 * Inicializa esta instancia
	 */
	private void inicializarCon(final VortexCore builder) {
		// Obtenemos un ID propio de componente
		this.idPropio = builder.crearIdDeComponente();

		// Creamos el filtro que rechaza mensajes propios y duplicados
		final MemoriaDeMensajes memoriaDeMensajes = builder.crearMemoriaDeMensajes();
		final EsMensajeNuevoDeOtroComponente condicionFiltro = EsMensajeNuevoDeOtroComponente.create(idPropio,
				memoriaDeMensajes);
		this.filtroRecibidos = builder.filtroDe(condicionFiltro);

		// Creamos la transformacion para asignar Ids a los mensajes enviados
		final GeneradorDeIdsDeMensajes generadorDeIdsParaMensajes = builder.crearGeneradorDeIdsParaMensajes(idPropio);
		this.transformadorEnviados = builder.transformadorPara(GenerarIdEnMensaje.create(generadorDeIdsParaMensajes));
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		filtroRecibidos.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#getConectorParaRecibirSinDuplicados()
	 */
	public Conectable getConectorParaRecibirSinDuplicados() {
		return filtroRecibidos;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#getIdPropio()
	 */
	public IdDeComponenteVortex getIdPropio() {
		return idPropio;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#getConectorParaEnviarConId()
	 */
	public Receptor getConectorParaEnviarConId() {
		return transformadorEnviados;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		transformadorEnviados.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#desconectar()
	 */
	public void desconectar() {
		transformadorEnviados.desconectar();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Identificador#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		transformadorEnviados.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados() {
		final ArrayList<Receptor> conectados = new ArrayList<Receptor>(1);
		conectados.add(transformadorEnviados.getConectado());
		return conectados;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(idPropio_FIELD, idPropio)
				.toString();
	}
}

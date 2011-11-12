/**
 * 20/08/2011 13:22:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.services;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.conectores.http.ComandoHttp;
import net.gaia.vortex.conectores.http.WrapperHttp;
import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.externals.json.InterpreteJackson;
import net.gaia.vortex.externals.json.InterpreteJson;
import net.gaia.vortex.model.messages.MensajesPendientes;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaPorId;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaPorInstancia;
import net.gaia.vortex.model.servidor.receptores.ReceptorVortex;
import net.gaia.vortex.persistibles.MensajeVortexPersistible;
import net.gaia.vortex.persistibles.ReceptorHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenpines.commons.exceptions.UnhandledConditionException;
import com.tenpines.commons.persistence.repositories.GenericRepository;

/**
 * Esta clase es la implementación del servicio de conexión http
 * 
 * @author D. García
 */
@Service
public class ServicioConexionHttpImpl implements ServicioConexionHttp {
	private static final Logger LOG = LoggerFactory.getLogger(ServicioConexionHttpImpl.class);

	@Autowired
	private GenericRepository genericRepository;

	private InterpreteJson interpreteJson;

	public InterpreteJson getInterpreteJson() {
		if (interpreteJson == null) {
			interpreteJson = InterpreteJackson.create();
		}
		return interpreteJson;
	}

	/**
	 * @see net.gaia.vortex.services.ServicioConexionHttp#interpretarComoComando(net.gaia.vortex.externals.http.OperacionHttp)
	 */
	@Override
	public ComandoHttp interpretarComoComando(final OperacionHttp pedido) {
		final WrapperHttp wrapper = obtenerWrapperDesde(pedido);
		if (wrapper == null) {
			// No es posible generar el comando
			return null;
		}
		final ReferenciaAReceptor localizador = obtenerLocalizadorPara(wrapper);
		final List<MensajeVortex> mensajes = obtenerMensajesAEnviarDe(wrapper);
		final ComandoHttp comando = ComandoHttp.create(localizador, mensajes);
		return comando;
	}

	/**
	 * Lee el parámetro del poste del pedido recibido construyendo el wrapper recibido como texto
	 * json
	 * 
	 * @param pedido
	 *            El pedido http recibdio
	 * @return El wrapper interpretado
	 */
	private WrapperHttp obtenerWrapperDesde(final OperacionHttp pedido) {
		final String wrapperComoJson = pedido.getWrapperJson();
		if (wrapperComoJson == null) {
			LOG.warn("Se recibió un pedido sin wrapper HTTP. Probablemente no sea un cliente Vortex?:{}", pedido);
			return null;
		}
		final WrapperHttp wrapper = getInterpreteJson().leerWrapper(wrapperComoJson);
		return wrapper;
	}

	/**
	 * @param pedido
	 * @param pendientes
	 * @param receptor
	 */
	private void enviarPendientesEn(final OperacionHttp pedido, final MensajesPendientes pendientes,
			final ReceptorHttp receptor) {
		final List<MensajeVortex> mensajesWrappeados = pendientes.getMensajes();
		final WrapperHttp wrapperRespuesta = WrapperHttp.create(receptor.getId(), mensajesWrappeados);
		final String contenidoResponse = getInterpreteJson().escribirWrapper(wrapperRespuesta);
		pedido.responder(contenidoResponse);
	}

	/**
	 * Obtiene el mensaje enviado en el pedido http si es que hay alguno
	 * 
	 * @param wrapper
	 *            El pedido recibido
	 * @return El mensaje interpretado o null si no hubo ninguno
	 */
	private List<MensajeVortex> obtenerMensajesAEnviarDe(final WrapperHttp wrapper) {
		final List<MensajeVortex> mensajesAEnviar = wrapper.getMensajes();
		if (mensajesAEnviar == null || mensajesAEnviar.isEmpty()) {
			// No envía mensajes
			return null;
		}
		LOG.debug("Acá debería validar los mensajes recibidos: {}", mensajesAEnviar);
		return mensajesAEnviar;
	}

	/**
	 * Obtiene un localizador que sabe como encontrar la instancia del receptor involucrado en la
	 * comunicación actual. Este localizador sabrá si obtenerlo de la base, si es uno nuevo, etc.
	 * 
	 * @param wrapper
	 *            El pedido del cual obtener la identificación del receptor
	 * @return El localizador del receptor
	 */
	private ReferenciaAReceptor obtenerLocalizadorPara(final WrapperHttp wrapper) {
		final Long idReceptor = wrapper.getId();
		if (idReceptor != null) {
			// Si se identifica con ID, ya debería estar registrado
			return ReferenciaPorId.create(genericRepository, idReceptor);
		}
		final ReceptorHttp receptor = ReceptorHttp.create();
		receptor.setGenericRepository(genericRepository);
		final ReferenciaPorInstancia porInstancia = ReferenciaPorInstancia.create(receptor);
		return porInstancia;
	}

	/**
	 * @see net.gaia.vortex.services.ServicioConexionHttp#enviarPendientesA(net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor,
	 *      net.gaia.vortex.externals.http.OperacionHttp)
	 */
	@Override
	@Transactional(readOnly = false)
	public void enviarPendientesA(final ReferenciaAReceptor localizador, final OperacionHttp pedido) {
		// Sabemos que es http por que si no, no llegaría a este servicio
		final ReceptorVortex localizado = localizador.localizar();
		ReceptorHttp receptor;
		try {
			receptor = (ReceptorHttp) localizado;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("Castee a receptro HTTP y parece que no era: " + localizado);
		}
		final List<MensajeVortexPersistible> pendientes = new ArrayList<MensajeVortexPersistible>(
				receptor.getPendientes());
		final MensajesPendientes mensajesPendientes = MensajesPendientes.create(pendientes);
		enviarPendientesEn(pedido, mensajesPendientes, receptor);
		receptor.quitarDePendientes(pendientes);
		genericRepository.save(receptor);
	}
}

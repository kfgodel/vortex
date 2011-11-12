/**
 * 20/08/2011 13:27:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.persistibles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import net.gaia.vortex.model.messages.MensajeRecibido;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.receptores.ReceptorVortex;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.tenpines.commons.persistence.repositories.GenericRepository;
import com.tenpines.integration.hibernate.repositories.persistibles.PersistibleSupport;

/**
 * Esta clase representa un receptor que accede a sus mensajes del servidor con requests HTTP y por
 * lo tanto son guardadas en la base entre requests
 * 
 * @author D. García
 */
@Entity
public class ReceptorHttp extends PersistibleSupport implements ReceptorVortex {

	public static final String tagsQueLeInteresan_FIELD = "tagsQueLeInteresan";

	@OneToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	private List<MensajeVortexPersistible> pendientes;
	public static final String pendientes_FIELD = "pendientes";

	@OneToMany(orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "RECEPTOR_TAGS_INTERESANTES")
	private Set<TagPersistible> tagsQueLeInteresan;

	@OneToMany(orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "RECEPTOR_TAGS_NOTIFICADOS")
	private Set<TagPersistible> tagsNotificados;

	@Transient
	private GenericRepository genericRepository;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime creationMoment;
	public static final String creationMoment_FIELD = "creationMoment";

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime momentoUltimaComunicacion;
	public static final String momentoUltimaComunicacion_FIELD = "momentoUltimaComunicacion";

	public DateTime getCreationMoment() {
		return creationMoment;
	}

	public void setCreationMoment(final DateTime creationMoment) {
		this.creationMoment = creationMoment;
	}

	public DateTime getMomentoUltimaComunicacion() {
		return momentoUltimaComunicacion;
	}

	public void setMomentoUltimaComunicacion(final DateTime momentoUltimaComunicacion) {
		this.momentoUltimaComunicacion = momentoUltimaComunicacion;
	}

	public GenericRepository getGenericRepository() {
		return genericRepository;
	}

	public void setGenericRepository(final GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

	public Set<TagPersistible> getTagsQueLeInteresan() {
		if (tagsQueLeInteresan == null) {
			tagsQueLeInteresan = new LinkedHashSet<TagPersistible>();
		}
		return tagsQueLeInteresan;
	}

	public void setTagsQueLeInteresan(final Set<TagPersistible> tagsQueLeInteresan) {
		this.tagsQueLeInteresan = tagsQueLeInteresan;
	}

	public List<MensajeVortexPersistible> getPendientes() {
		if (pendientes == null) {
			pendientes = new ArrayList<MensajeVortexPersistible>();
		}
		return pendientes;
	}

	public void setPendientes(final List<MensajeVortexPersistible> pendientes) {
		this.pendientes = pendientes;
	}

	public static ReceptorHttp create() {
		final ReceptorHttp receptor = new ReceptorHttp();
		receptor.creationMoment = new DateTime();
		return receptor;
	}

	/**
	 * @see net.gaia.vortex.model.servidor.receptores.ReceptorVortex#recibir(net.gaia.vortex.model.messages.MensajeRecibido)
	 */
	@Override
	public void recibir(final MensajeRecibido mensajeRecibido) {
		final MensajeVortex mensaje = mensajeRecibido.getMensaje();
		final MensajeVortexPersistible mensajePersistible = MensajeVortexPersistible.create(mensaje);
		getPendientes().add(mensajePersistible);
		genericRepository.save(this);
	}

	/**
	 * @param pendientes2
	 */
	public void quitarDePendientes(final List<MensajeVortexPersistible> enviados) {
		for (final MensajeVortexPersistible enviado : enviados) {
			getPendientes().remove(enviado);
		}
	}

	/**
	 * Devuelve el tag del nombre indicado
	 * 
	 * @param tagNuevo
	 *            El nombre del tag buscado
	 * @return El tag de este receptor con ese nombre o null si no existe ninguno
	 */
	public TagPersistible getTag(final String tagNuevo) {
		final Set<TagPersistible> intereses = getTagsQueLeInteresan();
		for (final TagPersistible interes : intereses) {
			if (interes.getValor().equals(tagNuevo)) {
				return interes;
			}
		}
		return null;
	}

	/**
	 * Agrega un nuevo tag a este receptor
	 * 
	 * @param tagNuevo
	 *            El tag a agregar
	 */
	public void agregarTag(final String tagNuevo) {
		final TagPersistible nuevo = TagPersistible.create(tagNuevo, this);
		getTagsQueLeInteresan().add(nuevo);
	}

	/**
	 * Elimina el tag de los intereses de este receptor si existe alguno
	 * 
	 * @param tags
	 */
	public void quitarTag(final String tag) {
		final Set<TagPersistible> intereses = getTagsQueLeInteresan();
		for (final Iterator<TagPersistible> iterator = intereses.iterator(); iterator.hasNext();) {
			final TagPersistible tagPersistible = iterator.next();
			if (tagPersistible.getValor().equals(tag)) {
				tagPersistible.setReceptor(null);
				iterator.remove();
				// Después de quitarlo no quedan más
				return;
			}
		}
	}

	public Set<TagPersistible> getTagsNotificados() {
		if (tagsNotificados == null) {
			tagsNotificados = new LinkedHashSet<TagPersistible>();
		}
		return tagsNotificados;
	}

	public void setTagsNotificados(final Set<TagPersistible> tagsNotificados) {
		this.tagsNotificados = tagsNotificados;
	}

	/**
	 * De los tags pasados genera un conjunto con los que no figuran en los tags notificados en este
	 * receptor
	 * 
	 * @param tagsActualesDelServer
	 *            Tags
	 * @return
	 */
	public Set<String> detectarMensajesNuevosEn(final Set<String> tagsActualesDelServer) {
		final Set<String> tagsNuevos = new LinkedHashSet<String>();
		for (final String tagDelServer : tagsActualesDelServer) {
			final TagPersistible tagNotificado = getTagNotificadoPara(tagDelServer);
			if (tagNotificado == null) {
				tagsNuevos.add(tagDelServer);
			}
		}

		return tagsNuevos;
	}

	/**
	 * Devuelve el tag previamente notificado para el valor pasado
	 */
	private TagPersistible getTagNotificadoPara(final String tagDelServer) {
		final Set<TagPersistible> allNotificados = getTagsNotificados();
		for (final TagPersistible tagNotificado : allNotificados) {
			if (tagNotificado.getValor().equals(tagDelServer)) {
				return tagNotificado;
			}
		}
		return null;
	}

	/**
	 * @param tagsActualesDelServer
	 * @return
	 */
	public Set<String> detectarMensajesQuitadosEn(final Set<String> tagsActualesDelServer) {
		final Set<String> tagsQuitados = new LinkedHashSet<String>();
		final Set<TagPersistible> allNotificados = getTagsNotificados();
		for (final TagPersistible tagNotificado : allNotificados) {
			final String valorTagNotificado = tagNotificado.getValor();
			if (!tagsActualesDelServer.contains(valorTagNotificado)) {
				tagsQuitados.add(valorTagNotificado);
			}
		}
		return tagsQuitados;
	}

	/**
	 * Agrega el tag indicado como notificado
	 * 
	 * @param tagAgregado
	 *            El tag agregado
	 */
	public void agregarNotificado(final String tagAgregado) {
		final TagPersistible nuevo = TagPersistible.create(tagAgregado, null);
		getTagsNotificados().add(nuevo);
	}

	/**
	 * @param tagQuitado
	 */
	public void quitarNotificado(final String tagQuitado) {
		final Set<TagPersistible> intereses = getTagsNotificados();
		for (final Iterator<TagPersistible> iterator = intereses.iterator(); iterator.hasNext();) {
			final TagPersistible tagPersistible = iterator.next();
			if (tagPersistible.getValor().equals(tagQuitado)) {
				tagPersistible.setReceptor(null);
				iterator.remove();
				// Después de quitarlo no quedan más
				return;
			}
		}
	}

}

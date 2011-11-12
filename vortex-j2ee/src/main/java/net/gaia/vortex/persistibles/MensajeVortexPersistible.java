/**
 * 20/08/2011 17:24:41 Copyright (C) 2011 Darío L. García
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
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import net.gaia.vortex.model.messages.protocolo.MensajeVortex;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.tenpines.integration.hibernate.repositories.persistibles.PersistibleSupport;

/**
 * 
 * @author D. García
 */
@Entity
public class MensajeVortexPersistible extends PersistibleSupport {

	@OneToOne
	@Cascade(CascadeType.ALL)
	private ContenidoVortexPersistible contenido;
	public static final String contenido_FIELD = "contenido";

	@ElementCollection
	private List<String> tagsDestino;

	@OneToOne
	@Cascade(CascadeType.ALL)
	private IdVortexPersistible identificacion;
	public static final String identificacion_FIELD = "identificacion";

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime creationMoment;
	public static final String creationMoment_FIELD = "creationMoment";

	public ContenidoVortexPersistible getContenido() {
		return contenido;
	}

	public List<String> getTagsDestino() {
		return tagsDestino;
	}

	public void setTagsDestino(final List<String> tagsDestino) {
		this.tagsDestino = tagsDestino;
	}

	public void setContenido(final ContenidoVortexPersistible contenido) {
		this.contenido = contenido;
	}

	public DateTime getCreationMoment() {
		return creationMoment;
	}

	public void setCreationMoment(final DateTime creationMoment) {
		this.creationMoment = creationMoment;
	}

	public IdVortexPersistible getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(final IdVortexPersistible identificacion) {
		this.identificacion = identificacion;
	}

	public static MensajeVortexPersistible create(final MensajeVortex mensajeVortex) {
		final MensajeVortexPersistible mensaje = new MensajeVortexPersistible();
		mensaje.contenido = ContenidoVortexPersistible.create(mensajeVortex.getContenido());
		mensaje.identificacion = IdVortexPersistible.create(mensajeVortex.getIdentificacion());
		mensaje.tagsDestino = mensajeVortex.getTagsDestino();
		mensaje.creationMoment = new DateTime();
		return mensaje;
	}

	/**
	 * Devuelve una versión enviable de este mensaje
	 */
	public MensajeVortex toMensajeVortex() {
		final MensajeVortex mensajeVortex = new MensajeVortex();
		mensajeVortex.setContenido(getContenido().toContenidoVortex());
		mensajeVortex.setIdentificacion(identificacion.toIdentificacionVortex());
		mensajeVortex.setTagsDestino(new ArrayList<String>(getTagsDestino()));
		return mensajeVortex;
	}

}

/**
 * 30/10/2012 12:45:03 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router.impl.mensajes;

import net.gaia.vortex.tests.router.impl.patas.filtros.FiltroPorStrings;

/**
 * Esta clase representa el mensaje generico para los pasos del handshake
 * 
 * @author D. García
 */
public class MensajeDeHandshake extends MensajeSupport {

	/**
	 * Id local al enviarlo, id remoto al recibirlo
	 */
	private Long idDePata;

	private MensajeDeHandshake pasoAnterior;

	private Integer numeroDePaso;

	public Integer getNumeroDePaso() {
		return numeroDePaso;
	}

	public void setNumeroDePaso(final Integer numeroDePaso) {
		this.numeroDePaso = numeroDePaso;
	}

	public Long getIdDePata() {
		return idDePata;
	}

	public void setIdDePata(final Long idDePata) {
		this.idDePata = idDePata;
	}

	public MensajeDeHandshake getPasoAnterior() {
		return pasoAnterior;
	}

	public void setPasoAnterior(final MensajeDeHandshake pasoAnterior) {
		this.pasoAnterior = pasoAnterior;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

}

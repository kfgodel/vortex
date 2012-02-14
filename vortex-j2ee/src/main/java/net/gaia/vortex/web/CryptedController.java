/**
 * 13/02/2012 19:51:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.externals.http.OperacionHttpJ2ee;
import net.gaia.vortex.http.crypted.CryptedHttpTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Esta clase representa el controller de comunicaciones utilizando encriptado para los datos
 * 
 * @author D. García
 */
@Controller
public class CryptedController {
	private static final Logger LOG = LoggerFactory.getLogger(CryptedController.class);

	@Autowired
	private CryptedHttpTranslator translator;

	@RequestMapping(value = "keys")
	public void onKeysRequested(final HttpServletRequest request, final HttpServletResponse response) {
		LOG.debug("Llegó un request: \"" + request.getRequestURI() + "\"");

		final OperacionHttp pedido = OperacionHttpJ2ee.create(request, response);
		translator.grantKeys(pedido);
	}

	@RequestMapping(value = "crypted")
	public void onCryptedRequest(final HttpServletRequest request, final HttpServletResponse response) {
		LOG.debug("Llegó un request: \"" + request.getRequestURI() + "\"");

		final OperacionHttp pedido = OperacionHttpJ2ee.create(request, response);
		translator.translate(pedido);
	}

}

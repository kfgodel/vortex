package net.gaia.vortex.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.externals.http.OperacionHttpJ2ee;
import net.gaia.vortex.http.controller.NakedHttpTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller de Spring que atiende el primer request de un usuario (por defecto en el web.xml se
 * indica como página inicial el main.jsp), que por mapeos automáticos de URL dirije a este
 * controller para procesar el request
 * 
 * @author D. García
 */
@Controller
public class NakedController {
	private static final Logger LOG = LoggerFactory.getLogger(NakedController.class);

	@Autowired
	private NakedHttpTranslator translator;

	@RequestMapping(value = "naked")
	public void onPlainRequest(final HttpServletRequest request, final HttpServletResponse response) {
		LOG.debug("Llegó un request: \"" + request.getRequestURI() + "\"");

		final OperacionHttp pedido = OperacionHttpJ2ee.create(request, response);
		translator.translate(pedido);
	}
}

package net.gaia.vortex.sets.tests;

import java.util.Map;

import junit.framework.Assert;

import net.gaia.vortex.router.impl.sets.converter.ConversorDeCondiciones;
import net.gaia.vortex.router.impl.sets.converter.MetadataDeCondiciones;
import net.gaia.vortex.sets.impl.ValorEsperadoIgual;
import net.gaia.vortex.sets.reflection.accessors.PropertyAccessor;

import org.junit.Test;

/**
 * Esta clase prueba las conversiones de condiciones en mapas serializables
 * 
 * @author kfgodel
 */
public class TestConversionDeCondiciones {
	
	private ConversorDeCondiciones conversor;

	@Test
	public void deberiaConvertirEnMapaUnaCondicionPorClaveValor() {
		String valorEsperado = "valor";
		String claveEsperada = "clave";
		ValorEsperadoIgual condicionClaveValor = ValorEsperadoIgual.create(valorEsperado, PropertyAccessor.create(claveEsperada));
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicionClaveValor);
		Assert.assertEquals(MetadataDeCondiciones.TipoClaveValor.TIPO_CLAVE_VALOR, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		Assert.assertEquals(claveEsperada, mapaConvertido.get(MetadataDeCondiciones.TipoClaveValor.ATRIBUTO_CLAVE));
		Assert.assertEquals(valorEsperado, mapaConvertido.get(MetadataDeCondiciones.TipoClaveValor.ATRIBUTO_VALOR));
	}
	
}

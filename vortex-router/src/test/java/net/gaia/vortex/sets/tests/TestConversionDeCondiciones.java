package net.gaia.vortex.sets.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.router.impl.sets.converter.ConversorDeCondiciones;
import net.gaia.vortex.router.impl.sets.converter.MetadataDeCondiciones;
import net.gaia.vortex.sets.impl.And;
import net.gaia.vortex.sets.impl.Not;
import net.gaia.vortex.sets.impl.Or;
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
	public void deberiaConvertirEnMapaUnTrue(){
		Condicion condicion = SiempreTrue.getInstancia();
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicion);
		Assert.assertEquals(MetadataDeCondiciones.TipoTrue.TIPO_TRUE, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
	}

	@Test
	public void deberiaConvertirEnMapaUnFalse(){
		Condicion condicion = SiempreFalse.getInstancia();
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicion);
		Assert.assertEquals(MetadataDeCondiciones.TipoFalse.TIPO_FALSE, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
	}

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

	@Test
	public void deberiaConvertirEnUnMapaUnAndVacio(){
		And condicionAnd = And.create(new ArrayList<Condicion>());
		
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicionAnd);
		Assert.assertEquals(MetadataDeCondiciones.TipoAnd.TIPO_AND, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> filtros = (List<Map<String, Object>>) mapaConvertido.get(MetadataDeCondiciones.TipoAnd.ATRIBUTO_FILTROS);
		Assert.assertNotNull(filtros);
		
		Assert.assertEquals(0, filtros.size());
	}
	
	@Test
	public void deberiaConvertirEnUnMapaUnOrVacio(){
		Or condicionOr = Or.create(new ArrayList<Condicion>());
		
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicionOr);
		Assert.assertEquals(MetadataDeCondiciones.TipoOr.TIPO_OR, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> filtros = (List<Map<String, Object>>) mapaConvertido.get(MetadataDeCondiciones.TipoOr.ATRIBUTO_FILTROS);
		Assert.assertNotNull(filtros);
		
		Assert.assertEquals(0, filtros.size());
	}

	@Test
	public void deberiaConvertirEnUnMapaUnNotVacio(){
		Not condicionNot = Not.de(null);
		
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicionNot);
		Assert.assertEquals(MetadataDeCondiciones.TipoNot.TIPO_NOT, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		
		@SuppressWarnings("unchecked")
		Map<String, Object> subCondicion = (Map<String, Object>) mapaConvertido.get(MetadataDeCondiciones.TipoNot.ATRIBUTO_FILTRO);
		Assert.assertNull(subCondicion);
	}

	
	
	@Test
	public void deberiaConvertirEnMapasAnidadosSiHayVariasCondiciones() {
		And condicionAnd = And.create(SiempreTrue.getInstancia(), SiempreFalse.getInstancia());
		
		Map<String, Object> mapaConvertido = conversor.convertirAMapa(condicionAnd);
		Assert.assertEquals(MetadataDeCondiciones.TipoAnd.TIPO_AND, mapaConvertido.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> filtros = (List<Map<String, Object>>) mapaConvertido.get(MetadataDeCondiciones.TipoAnd.ATRIBUTO_FILTROS);
		Assert.assertNotNull(filtros);
		
		Assert.assertEquals(2, filtros.size());
		
		Map<String, Object> mapa1 = filtros.get(0);
		Assert.assertEquals(MetadataDeCondiciones.TipoTrue.TIPO_TRUE, mapa1.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
		
		Map<String, Object> mapa2 = filtros.get(1);
		Assert.assertEquals(MetadataDeCondiciones.TipoFalse.TIPO_FALSE, mapa2.get(MetadataDeCondiciones.ATRIBUTO_TIPO));
	}
	
}

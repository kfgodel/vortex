/**
 * 16/01/2013 13:08:30 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.reflection.types.impl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.reflection.types.Linaje;
import ar.com.dgarcia.lang.reflection.types.Tipo;
import ar.com.dgarcia.lang.reflection.types.Tipos;
import ar.com.dgarcia.lang.reflection.types.VariableGenerics;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase intenta implementar el tipo para todas sus variantes
 * 
 * @author D. García
 */
public class TipoComun implements Tipo {

	private Type genericType;
	public static final String genericType_FIELD = "genericType";

	private Class<?> concreteClass;
	public static final String concreteClass_FIELD = "concreteClass";

	private List<Tipo> parametrosGenerics;
	public static final String parametrosGenerics_FIELD = "parametrosGenerics";

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getLinaje()
	 */
	
	public Linaje getLinaje() {
		final ArmadorDeLinaje armador = ArmadorDeLinaje.create(this);
		return armador.armarLinaje();
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getParametrosGenerics()
	 */
	
	public List<Tipo> getParametrosGenerics() {
		if (parametrosGenerics == null) {
			parametrosGenerics = deducirParametrosGenerics();
		}
		return parametrosGenerics;
	}

	/**
	 * Genera una lista con los tipos que parametrizan este tipo, si corresponde
	 * 
	 * @return la lista de tipos correspondientes a los parametros de este tipo
	 */
	private List<Tipo> deducirParametrosGenerics() {
		if (!(genericType instanceof ParameterizedType)) {
			// No es un tipo parametrizable, no tiene parámetros
			return Collections.emptyList();
		}
		final ParameterizedType tipoParametrizado = (ParameterizedType) genericType;
		final Type[] argumentosDeTipo = tipoParametrizado.getActualTypeArguments();
		final ArrayList<Tipo> parametrosGenerics = new ArrayList<Tipo>(argumentosDeTipo.length);
		for (int i = 0; i < argumentosDeTipo.length; i++) {
			final Type argumentoDeTipo = argumentosDeTipo[i];
			final Tipo tipo = Tipos.desdeType(argumentoDeTipo);
			parametrosGenerics.add(tipo);
		}
		return parametrosGenerics;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getClassInstance()
	 */
	
	public Class<?> getClassInstance() {
		return concreteClass;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getSuperTipo()
	 */
	
	public Tipo getSuperTipo() {
		if (concreteClass == null) {
			// Este tipo no tiene supertipo o no podemos saberlo
			return null;
		}
		final Type superType = concreteClass.getGenericSuperclass();
		if (superType == null) {
			// No tiene un supertipo
			return null;
		}
		final Tipo superTipoDeclarado = Tipos.desdeType(superType);
		final List<VariableGenerics> variablesGenerics = getVariablesGenerics();
		final Tipo superTipoImplementado = superTipoDeclarado.reemplazando(variablesGenerics);
		return superTipoImplementado;
	}

	public static TipoComun create(final Type tipoGenerico, final Class<?> claseConcreta) {
		final TipoComun tipo = new TipoComun();
		tipo.concreteClass = claseConcreta;
		tipo.genericType = tipoGenerico;
		return tipo;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getTipoDelArray()
	 */
	
	public Tipo getTipoDelArray() {
		if (genericType instanceof GenericArrayType) {
			final GenericArrayType genericArrayType = (GenericArrayType) genericType;
			final Type genericComponentType = genericArrayType.getGenericComponentType();
			return Tipos.desdeType(genericComponentType);
		}
		if (concreteClass == null) {
			// Este tipo no es un array o no podemos saber su componente
			return null;
		}
		final Class<?> componentType = concreteClass.getComponentType();
		if (componentType == null) {
			return null;
		}
		final Tipo tipoDeComponente = Tipos.desdeType(componentType);
		return tipoDeComponente;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getVariablesGenerics()
	 */
	
	public List<VariableGenerics> getVariablesGenerics() {
		if (concreteClass == null) {
			// Este tipo no tiene variables o no podemos saberlas
			return Collections.emptyList();
		}

		final TypeVariable<?>[] typeParameters = concreteClass.getTypeParameters();
		if (typeParameters.length == 0) {
			return Collections.emptyList();
		}

		final List<VariableGenerics> variables = new ArrayList<VariableGenerics>(typeParameters.length);
		final List<Tipo> valoresReemplazados = getParametrosGenerics();
		for (int i = 0; i < typeParameters.length; i++) {
			final TypeVariable<?> typeParameter = typeParameters[i];
			final String nombre = typeParameter.getName();
			Tipo valor;
			try {
				valor = valoresReemplazados.get(i);
			} catch (final IndexOutOfBoundsException e) {
				throw new UnhandledConditionException("Los parametros " + parametrosGenerics + " de este tipo[" + this
						+ "] no coinciden en cantidad con las variables declaradas: " + Arrays.toString(typeParameters));
			}
			final VariableGenerics variable = VariableImpl.create(nombre, valor);
			variables.add(variable);
		}

		return variables;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#reemplazando(java.util.List)
	 */
	
	public Tipo reemplazando(final List<VariableGenerics> variablesReemplazo) {
		final List<VariableGenerics> variablesDeclaradas = getVariablesGenerics();
		if (variablesDeclaradas.isEmpty()) {
			// Si no tenemos variables que reemplazar
			return this;
		}

		// Reemplazamos los valores para cada variable declarada
		boolean huboReemplazo = false;
		final List<Tipo> parametrosReemplazados = new ArrayList<Tipo>(variablesDeclaradas.size());
		for (final VariableGenerics variableDeclarada : variablesDeclaradas) {
			// Si no tiene reemplazo queda como esta
			Tipo valorFinal = variableDeclarada.getValor();

			// Si el valor actual es una variable, vemos con que nombre se lo reemplaza
			final String nombreDeReemplazoParaVariableDeclarada = valorFinal.getNombreDeVariable();

			// Buscamos el valor actual para el reemplazo
			for (final VariableGenerics variableDeReemplazo : variablesReemplazo) {
				if (variableDeReemplazo.getNombre().equals(nombreDeReemplazoParaVariableDeclarada)) {
					// Es la misma variable, tomamos su valor como reemplazo
					valorFinal = variableDeReemplazo.getValor();
					huboReemplazo = true;
					break;
				}
			}
			parametrosReemplazados.add(valorFinal);
		}
		// Optimizacion para no perder info de tipo
		if (!huboReemplazo) {
			return this;
		}

		final TipoComun tipoReemplazado = TipoComun.create(concreteClass, concreteClass);
		tipoReemplazado.parametrosGenerics = parametrosReemplazados;
		return tipoReemplazado;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#representaA(java.lang.Class)
	 */
	
	public boolean representaA(final Class<?> clase) {
		if (concreteClass == null) {
			// No representamos ninguna clase, incluso si pasan null
			return false;
		}
		return concreteClass.equals(clase);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(genericType_FIELD, genericType).con(concreteClass_FIELD, concreteClass)
				.con(parametrosGenerics_FIELD, parametrosGenerics).toString();
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Tipo#getNombreDeVariable()
	 */
	
	public String getNombreDeVariable() {
		if (genericType instanceof TypeVariable) {
			final TypeVariable<?> genericVariableType = (TypeVariable<?>) genericType;
			final String nombreDeVariable = genericVariableType.getName();
			return nombreDeVariable;
		}
		return null;
	}
}

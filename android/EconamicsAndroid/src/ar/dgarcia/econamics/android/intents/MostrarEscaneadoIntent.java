/**
 * 24/09/2012 14:19:06 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.econamics.android.intents;

import android.content.Context;
import android.content.Intent;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.econamics.android.MostrarEscaneadoActivity;

/**
 * Esta clase representa el intent para mostrar un archivo escaneado
 * 
 * @author D. García
 */
public class MostrarEscaneadoIntent extends Intent {

	public static final String ID_DEL_ARCHIVO_KEY = "ID_DEL_ARCHIVO_KEY";

	public MostrarEscaneadoIntent(Intent copia) {
		super(copia);
	}

	public MostrarEscaneadoIntent(Context contexto, Long idDelArchivo) {
		super(contexto, MostrarEscaneadoActivity.class);
		setIdDelArchivo(idDelArchivo);
	}

	public void setIdDelArchivo(Long idDelArchivo) {
		putExtra(ID_DEL_ARCHIVO_KEY, idDelArchivo);
	}

	public Long getIdDelArchivo() {
		long idDelArchivo = getLongExtra(ID_DEL_ARCHIVO_KEY, -1);
		if (idDelArchivo == -1) {
			throw new UnhandledConditionException("No existe id del archivo en el intent");
		}
		return idDelArchivo;
	}
}

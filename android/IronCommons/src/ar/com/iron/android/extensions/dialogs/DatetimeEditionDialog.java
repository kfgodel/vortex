/**
 * 29/03/2011 20:30:29 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.dialogs;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import ar.com.iron.R;
import ar.com.iron.android.extensions.widgets.datetime.DateTimeController;

/**
 * Esta clase permite implementar la edición de una fecha completa
 * 
 * @author D. García
 */
public class DatetimeEditionDialog extends CustomDialog {

	private final DateTimeController dateTimeController;

	public DatetimeEditionDialog(Context context) {
		super(context);
		dateTimeController = DateTimeController.create();
	}

	/**
	 * @see ar.com.iron.android.extensions.dialogs.CustomDialog#getDialogTitleOrId()
	 */
	@Override
	protected Object getDialogTitleOrId() {
		return "Cambiar fecha";
	}

	/**
	 * @see ar.com.iron.android.extensions.dialogs.CustomDialog#setUpComponents()
	 */
	@Override
	protected void setUpComponents() {
		dateTimeController.initUiFrom(getContentView());
	}

	/**
	 * @see ar.com.iron.android.extensions.dialogs.CustomDialog#getDialogLayoutId()
	 */
	@Override
	protected int getDialogLayoutId() {
		return R.layout.datetime_edition_dialog;
	}

	/**
	 * Devuelve el valor actual como un calendario
	 * 
	 * @return El valor definido en este diálogo para la fecha como calendario
	 */
	public Calendar getValueAsCalendar() {
		return dateTimeController.getCalendar();
	}

	/**
	 * Devuelve el valor actual de este diálogo como fecha
	 * 
	 * @return El valor definido en la edición como fecha
	 */
	public Date getValueAsDate() {
		return getValueAsCalendar().getTime();
	}

	/**
	 * Devuelve el valor actual de este diálogo como milisegundos desde el 70
	 * 
	 * @return El valor en milisegundos
	 */
	public long getValueAsMillis() {
		return getValueAsCalendar().getTimeInMillis();
	}

	/**
	 * @see ar.com.iron.android.extensions.dialogs.CustomDialog#getCancelButtonId()
	 */
	@Override
	public Integer getCancelButtonId() {
		return R.id.datefields_btn_cancel;
	}

	/**
	 * @see ar.com.iron.android.extensions.dialogs.CustomDialog#getOkButtonId()
	 */
	@Override
	public Integer getOkButtonId() {
		return R.id.datefields_btn_accept;
	}

	/**
	 * Establece el valor actual de este diálogo a partir de la fecha indicada
	 * 
	 * @param valorActual
	 */
	public void setValue(Date valorActual) {
		getValueAsCalendar().setTime(valorActual);
	}

	/**
	 * Establece el valor actual de este diálogo a partir del calendario indicado
	 * 
	 * @param valorActual
	 */
	public void setValue(Calendar valorActual) {
		dateTimeController.setCalendar(valorActual);
	}

	/**
	 * Establece el valor actual de este diálogo a partir de los milisegundos indicados
	 * 
	 * @param valorActual
	 */
	public void setValue(long valorActual) {
		getValueAsCalendar().setTimeInMillis(valorActual);
	}

}

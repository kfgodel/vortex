/**
 * 28/03/2011 23:41:37 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.widgets.datetime;

import java.util.Calendar;

import android.view.View;
import ar.com.iron.R;
import ar.com.iron.helpers.StringHelper;

/**
 * Esta clase representa un controller que agrupa a los controllers de las porciones de una fecha
 * 
 * @author D. García
 */
public class DateTimeController {

	private Calendar calendar;
	private DateFieldController[] dateFieldControllers;
	private int editableIndex;

	/**
	 * Crea el controlador de los campos de las fechas a partir de la vista que contiene a los
	 * widgets identificados con el id de commons para cada atributo de la fecha
	 * 
	 * @param parentView
	 *            Vista desde la que se pedirán los widgets para cada parte de la fecha por id
	 * @return El controller de la fecha creado
	 */
	public static DateTimeController create() {
		DateTimeController controller = new DateTimeController();
		controller.calendar = Calendar.getInstance();
		controller.dateFieldControllers = new DateFieldController[5];
		controller.editableIndex = -1;
		return controller;
	}

	/**
	 * Inicializa los sub controles de este controlador que son mostrados en la vista, utilizando
	 * los IDs predefinidos para cada uno
	 * 
	 * @param parentView
	 *            La vista desde la cual obtener los widgets para cada parte de la fecha
	 */
	public void initUiFrom(View parentView) {
		setWidgetsFor(Calendar.YEAR, 0, R.id.datefields_btn_year_plus, R.id.datefields_btn_year_minus,
				R.id.datefields_txt_year, parentView);
		setWidgetsFor(Calendar.MONTH, 1, R.id.datefields_btn_month_plus, R.id.datefields_btn_month_minus,
				R.id.datefields_txt_month, parentView);
		setWidgetsFor(Calendar.DAY_OF_MONTH, 2, R.id.datefields_btn_day_plus, R.id.datefields_btn_day_minus,
				R.id.datefields_txt_day, parentView);
		setWidgetsFor(Calendar.HOUR_OF_DAY, 3, R.id.datefields_btn_hour_plus, R.id.datefields_btn_hour_minus,
				R.id.datefields_txt_hour, parentView);
		setWidgetsFor(Calendar.MINUTE, 4, R.id.datefields_btn_minute_plus, R.id.datefields_btn_minute_minus,
				R.id.datefields_txt_minute, parentView);
		actualizarVista();
	}

	/**
	 * Actualiza el estado de la vista a los valores actuales
	 */
	private void actualizarVista() {
		for (int i = 0; i < dateFieldControllers.length; i++) {
			DateFieldController dateField = dateFieldControllers[i];
			if (i == editableIndex) {
				// El control es editable
				dateField.mostrarEditable();
			} else {
				dateField.mostrarNoEditable();
			}
			int calendarField = dateField.getCalendarField();
			String fieldValue = calcularFieldValue(calendarField);
			dateField.setValue(fieldValue);
		}
	}

	/**
	 * @param calendarField
	 * @return
	 */
	private String calcularFieldValue(int calendarField) {
		int currentValue = calendar.get(calendarField);
		int cantidadCeros = 2;
		if (calendarField == Calendar.YEAR) {
			cantidadCeros = 4;
		}
		if (calendarField == Calendar.MONTH) {
			// Los meses los cuenta desde 0, a diferencia del resto de los campos
			currentValue++;
		}
		String formatted = StringHelper.padInteger(currentValue, cantidadCeros);
		return formatted;
	}

	/**
	 * Establece los widgets a utilizar para mostrar el estado de un campo de la fecha y modificarlo
	 * 
	 * @param calendarField
	 *            Campo del calendar representado
	 * @param fieldIndex
	 *            Índice en el array de controladores
	 * @param plusButtonId
	 *            ID del botón para la suma
	 * @param minusButtonId
	 *            ID del botón para la resta
	 * @param textId
	 *            Id del texto para mostrar el valor
	 * @param parentView
	 *            Vista de la que se obtendrán los controles
	 */
	private void setWidgetsFor(int calendarField, int fieldIndex, int plusButtonId, int minusButtonId, int textId,
			View parentView) {
		DateFieldController dateField = DateFieldController.create(calendar, calendarField, plusButtonId,
				minusButtonId, textId, parentView);
		dateField.setListener(new OnDateFieldChangeListener() {
			public void onDateFieldChanged(int calendarField, int newDelta) {
				onCampoModificado(calendarField, newDelta);
			}

			public void onDateFieldSelected(DateFieldController dateFieldController) {
				onCampoElegido(dateFieldController);
			}
		});
		dateFieldControllers[fieldIndex] = dateField;
	}

	/**
	 * Invocado cuando se selecciona un campo, si el campo seleccionado es distinto del actual, se
	 * cambia el campo editable
	 * 
	 * @param selectedController
	 *            El campo seleccionado para edición
	 */
	protected void onCampoElegido(DateFieldController selectedController) {
		int selectedIndex = 0;
		for (; selectedIndex < dateFieldControllers.length; selectedIndex++) {
			DateFieldController dateField = dateFieldControllers[selectedIndex];
			if (dateField == selectedController) {
				break;
			}
		}
		if (selectedIndex >= dateFieldControllers.length) {
			// Nos mandaron fruta
			return;
		}
		if (selectedIndex == editableIndex) {
			// Es el mismo que ya está activado
			return;
		}
		// Hubo un cambio de campo
		editableIndex = selectedIndex;
		actualizarVista();
	}

	/**
	 * Invocado al producirse un cambio en alguno de los campos de la fecha
	 * 
	 * @param calendarField
	 *            Campo de la fecha modificado
	 * @param newDelta
	 *            La variación en el campo
	 */
	protected void onCampoModificado(int calendarField, int newDelta) {
		calendar.add(calendarField, newDelta);
		actualizarVista();
	}

	/**
	 * Habilita la edición del campo indicado por índice, y deshabilita la edición del resto
	 * 
	 * @param indiceElegido
	 *            Índice del campo elegido
	 */
	public void habilitarEdicionDeCampo(int indiceElegido) {
		if (indiceElegido < 0 || indiceElegido >= dateFieldControllers.length) {
			throw new IllegalArgumentException("El indice elegido debe estar entre 0 y " + dateFieldControllers.length
					+ ": " + indiceElegido);
		}
		editableIndex = indiceElegido;
		actualizarVista();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

}

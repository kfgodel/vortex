/**
 * 15/11/2011 23:44:17 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.time;

import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa una magnitud de tiempo
 * 
 * @author D. García
 */
public class TimeMagnitude {
	private TimeUnit timeUnit;
	private long quantity;

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(final TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(final long quantity) {
		this.quantity = quantity;
	}

	/**
	 * Devuelve la cantidad que representa esta magnitud en milisegundos
	 * 
	 * @return La cantidad expresada en milisegundos
	 */
	public long getMillis() {
		final long millis = this.timeUnit.toMillis(this.quantity);
		return millis;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(": ");
		builder.append(quantity);
		builder.append(" ");
		builder.append(timeUnit);
		return builder.toString();
	}

	public static TimeMagnitude of(final long quantity, final TimeUnit unit) {
		final TimeMagnitude magnitud = new TimeMagnitude();
		magnitud.quantity = quantity;
		magnitud.timeUnit = unit;
		return magnitud;
	}

	public static TimeMagnitude of(final int quantity, final TimeUnit unit) {
		return of((long) quantity, unit);
	}

	/**
	 * Suma a esta magnitud la cantidad de tiempo indicada por la magnitud pasada y expresando el
	 * resultado en la unidad de esta instancia
	 * 
	 * @param other
	 *            La magnitud de tiempo añadida a esta
	 * @return El resultado de la suma de los tiempos en la unidad de esta instancia
	 */
	public TimeMagnitude plus(final TimeMagnitude other) {
		final long otherQuantityInThisUnit = other.getQuantityIn(this.timeUnit);
		final long summedValue = this.quantity + otherQuantityInThisUnit;
		final TimeMagnitude result = TimeMagnitude.of(summedValue, this.timeUnit);
		return result;
	}

	/**
	 * Expresa la cantidad de esta instancia en unidades del tipo de unidad de tiempo pasada
	 * 
	 * @param referenceUnit
	 *            El tipo de unidades a utilizar para expresar esta cantidad
	 * @return La cantidad de esta instancia expresada con la unidad pasada. Si esta cantidad no
	 *         llega a una unidad de la pasada se devuelve 0
	 */
	public long getQuantityIn(final TimeUnit referenceUnit) {
		final long inOtherUnit = referenceUnit.convert(this.quantity, this.timeUnit);
		return inOtherUnit;
	}
}

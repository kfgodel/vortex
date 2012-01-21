package net.gaia.taskprocessor.api;

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
	@Override
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
}

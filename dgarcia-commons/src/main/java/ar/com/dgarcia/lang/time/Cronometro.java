/*
 * Created on 18-abr-2004
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code
 * Generation&gt;Code and Comments
 */
package ar.com.dgarcia.lang.time;

/**
 * @author D. Garcia
 * 
 *         Esta clase permite medir el tiempo con métodos que ofrecen la funcionalidad de un
 *         cronometro. La precision depende de la plataforma pero es ofrecido hasta nanosegundos
 */
public class Cronometro {

	/**
	 * Horas en un dia
	 */
	public final static long HOUR_PER_DAY = 24;

	/**
	 * Cantidad de micros en un mili
	 */
	public final static long MICROS_PER_MILI = 1000;

	/**
	 * Milis en un segundo
	 */
	public final static long MILI_PER_SECOND = 1000;
	/**
	 * Minutos en una hora
	 */
	public final static long MIN_PER_HOUR = 60;
	/**
	 * Cantidad de nanosegundos en un microsegundo
	 */
	public final static long NANOS_PER_MICRO = 1000;
	/**
	 * segundos en un minuto
	 */
	public final static long SECONDS_PER_MIN = 60;

	/** Milisegundo en el que se puso en marcha el conteo */
	private long inicio;

	/** Acumulador de milisegundos para contar el tiempo */
	private long transcurrido;

	/**
	 * Inicializa el cronometro empezando a contar desde el momento de su creacion
	 */
	public Cronometro() {
		this.reset();
	}

	/**
	 * Devuelve la cantidad de milisegundos transcurridos al momento actual
	 * 
	 * @return Los milisegundos contados desde el inicio
	 */
	public long current() {
		return this.getTimeUnits() - this.inicio;
	}

	/**
	 * Devuelve la cantidad de unidades que marca el reloj actualmente en la mayor precision de la
	 * plataforma
	 * 
	 * @return Cantidad de nanosegundos
	 */
	private long getTimeUnits() {
		return System.nanoTime();
	}

	/**
	 * Pausa el conteo del tiempo hasta que se reanude la cuenta con start(). Si se llama el tiempo
	 * se cuenta por partes, y debe llamarse a total para saber cuanto es el total de tiempo
	 * 
	 * @return El tiempo transcurrido desde que se inicio la cuenta o desde que se paus� la ultima
	 *         vez
	 */
	public long pause() {
		this.transcurrido += this.current();
		this.start();
		return this.transcurrido;
	}

	/**
	 * Pone el contador cronometro a cero e Inicia el conteo del tiempo
	 */
	public void reset() {
		this.transcurrido = 0;
		this.start();
	}

	/**
	 * Marca el milisegundo actual como inicio del conteo del tiempo, inicia el conteo del tiempo
	 * 
	 * @return El momento actual en milisegundos
	 */
	public long start() {
		this.inicio = this.getTimeUnits();
		return this.inicio;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return Cronometro.niceFormatStr(this.current());
	}

	/**
	 * Devuelve la cantidad de milisegundos transcurridos contabilizados por el cronometro, debe
	 * pausarse para que devuelva el actual
	 * 
	 * @return El total de tiempo contado (incluyendo las veces que se detuvo el reloj)
	 */
	public long total() {
		return this.transcurrido;
	}

	/**
	 * Convierte una magnitud de milisegundos a segundos
	 * 
	 * @param nanos
	 *            El tiempo en milisegundos
	 * @return El tiempo en segundos
	 */
	public static long asSeconds(final long nanos) {
		return nanos / (NANOS_PER_MICRO * MICROS_PER_MILI * MILI_PER_SECOND);
	}

	/**
	 * Crea un nuevo cronometro que comienza a contar el tiempo desde el momento de su creacion
	 * 
	 * @return El nuevo cronometro corriendo
	 */
	public static Cronometro create() {
		final Cronometro cronometro = new Cronometro();
		cronometro.reset();
		return cronometro;
	}

	/**
	 * Genera una cadena formateada para mostrar tiempos largos
	 * 
	 * @param cantidad
	 *            Cantidad de nanosegundos
	 * @return un String con el siguiente formato "00d 00h:00m:00s.000ms [000us:000ns]". Las
	 *         magnitudes que son demasiado grandes para ser registradas son omitidas
	 */
	public static String niceFormatStr(long cantidad) {
		final StringBuilder cadena = new StringBuilder();
		long unidades;

		cadena.insert(0, "ns]");
		unidades = cantidad % NANOS_PER_MICRO;
		cadena.insert(0, unidades);
		cantidad /= NANOS_PER_MICRO;

		if (cantidad > 0) {
			cadena.insert(0, "us:");
			unidades = cantidad % MICROS_PER_MILI;
			cadena.insert(0, unidades);
			cantidad /= MICROS_PER_MILI;
		}
		cadena.insert(0, "[");

		if (cantidad > 0) {
			cadena.insert(0, "ms ");
			unidades = cantidad % MILI_PER_SECOND;
			cadena.insert(0, unidades);
			cantidad /= MILI_PER_SECOND;
		}
		if (cantidad > 0) {
			cadena.insert(0, "s.");
			unidades = cantidad % SECONDS_PER_MIN;
			cadena.insert(0, unidades);
			cantidad /= SECONDS_PER_MIN;
		}
		if (cantidad > 0) {
			cadena.insert(0, "m:");
			unidades = cantidad % MIN_PER_HOUR;
			cadena.insert(0, unidades);
			cantidad /= MIN_PER_HOUR;
		}
		if (cantidad > 0) {
			cadena.insert(0, "h:");
			unidades = cantidad % HOUR_PER_DAY;
			cadena.insert(0, unidades);
			cantidad /= HOUR_PER_DAY;
		}
		if (cantidad > 0) {
			cadena.insert(0, "d ");
			cadena.insert(0, cantidad);
		}

		return cadena.toString();
	}
}

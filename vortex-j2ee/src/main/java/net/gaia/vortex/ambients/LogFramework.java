/**
 * Created on: 18/12/2010 14:27:09 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package net.gaia.vortex.ambients;

import java.io.File;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import com.tenpines.commons.exceptions.UnhandledConditionException;

/**
 * Esta clase representa la api utilizada localmente para acceder al framework de logueo.<br>
 * Permite conocer la api realmente usada del framework de logueo
 * 
 * @author D. Garcia
 */
public class LogFramework {

	public static final String STDOUT_APPENDER_NAME = "STDOUT";
	public static final String ROLLING_APPENDER_NAME = "ROLLING_FILE";

	/**
	 * Blanquea el estado para que represente un archivo de configuración en blanco. Este método es
	 * llamado antes de configurar todos los appenders
	 */
	@Deprecated
	public void resetConfiguration() {
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.reset();
	}

	/**
	 * Crea y agrega al log raiz el appender por consola (que será heredado por todos)
	 * 
	 * @param layoutPattern
	 *            Patron con el cual generar el output
	 */
	@Deprecated
	public void addRootConsoleAppender(final String layoutPattern) {
		final Logger rootLogger = getRootLogger();

		// Agregamos el appender a la jerarquía de root
		final ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<ILoggingEvent>();
		addAppenderTo(rootLogger, consoleAppender, STDOUT_APPENDER_NAME);

		// Configuramos el patron de output
		configurePatternLayoutFor(consoleAppender, layoutPattern);
	}

	/**
	 * Quita del logger raiz (el que heredan todos) el appender indicado
	 * 
	 * @param appenderName
	 *            El nombre del appender a quitar
	 */
	public void removeFromRootLoggerAppender(final String appenderName) {
		final Logger rootLogger = getRootLogger();
		rootLogger.detachAppender(appenderName);
	}

	/**
	 * @return Devuelve el logger root de logback
	 */
	private Logger getRootLogger() {
		return getLoggerNamed(Logger.ROOT_LOGGER_NAME);
	}

	/**
	 * Devuelve el logger del nombre indicado
	 * 
	 * @param loggerName
	 *            El nombre que identifica el logger a devolver
	 * @return El logger del nombre pedido o null si no se encontró
	 */
	private Logger getLoggerNamed(final String loggerName) {
		// Podemos castear por que sabemos que estar logback
		Logger requestedLogger;
		try {
			requestedLogger = (Logger) LoggerFactory.getLogger(loggerName);
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("No estamos usando logback como framework de logueo?", e);
		}
		return requestedLogger;
	}

	/**
	 * Configura el patrón de logueo a utilizar con el appender indicado
	 * 
	 * @param appender
	 *            Appender en el que se configurará el layout
	 * @param layoutPattern
	 *            El patron a utilizar
	 */
	private void configurePatternLayoutFor(final Appender<ILoggingEvent> appender, final String layoutPattern) {
		final PatternLayout pl = new PatternLayout();
		final Context context = appender.getContext();
		pl.setContext(context);
		pl.setPattern(layoutPattern);
		pl.start();
		// appender.setLayout(pl);
		appender.start();
	}

	/**
	 * Devuelve una instancia del framework sobre el cual modificar la configuración
	 * 
	 * @return Una instancia wrapper sobre la cual modificar la configuración. Pueden ser instancias
	 *         distintas en distintas invocaciones
	 */
	public static LogFramework getInstance() {
		return new LogFramework();
	}

	/**
	 * Agrega un appender a archivo con rotación de archivo basado en el tiempo. Se asume que los
	 * archivos rotados van en un subdirectorio "historicos" creado si no existe
	 * 
	 * @param layoutPattern
	 *            Patron con el cual formatear el output
	 * @param maxDays
	 *            Cantidad máxima de días que se retienen los historicos (uno por día)
	 * @param baseFileName
	 *            Nombre del archivo principal del logueo. Los historicos se llamaran igual con el
	 *            anexo de la fecha en el nombre y en un subdirectorio "historicos"
	 */
	@Deprecated
	public void addRootRollingFileAppender(final String layoutPattern, final int maxDays, final String baseFileName) {

		final Logger rootLogger = getRootLogger();

		// Agregamos el appender a la jerarquía de root
		final RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();

		final TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
		addAppenderTo(rootLogger, rollingFileAppender, ROLLING_APPENDER_NAME);

		// Configuramos el archivo activo base
		rollingFileAppender.setFile(baseFileName);

		// Configuramos los historicos
		final String historicFileNames = calculateAndEnsureHistoricFilesFor(baseFileName);
		rollingPolicy.setFileNamePattern(historicFileNames);
		rollingPolicy.setMaxHistory(maxDays);
		rollingPolicy.setParent(rollingFileAppender);
		rollingPolicy.setContext(rollingFileAppender.getContext());
		rollingPolicy.start();

		rollingFileAppender.setRollingPolicy(rollingPolicy);

		// Configuramos el patron de output
		configurePatternLayoutFor(rollingFileAppender, layoutPattern);
	}

	/**
	 * Calcula el nombre que deberían utilizar los historicos a partir del nombre base
	 * 
	 * @param baseFileName
	 *            Nombre base a utilizar
	 * @return El patron para utilizar en los archivos historicos
	 */
	private String calculateAndEnsureHistoricFilesFor(final String baseFileName) {
		final String fsBaseName = baseFileName.replace('/', getDirectorySeparatorChar());
		final File baseFile = new File(fsBaseName);
		final File baseDirectory = baseFile.getParentFile();
		ensureDirectory(baseDirectory);

		final File historicSubDir = new File(baseDirectory.getAbsolutePath() + getDirectorySeparatorChar());
		ensureDirectory(historicSubDir);

		final String historicNamePattern = calculateHistoricNamePattern(baseFile);
		final String fsHisctoricPattern = historicSubDir.getAbsolutePath() + getDirectorySeparatorChar()
				+ historicNamePattern;
		final String historicFilePatternName = fsHisctoricPattern.replace('\\', '/');

		return historicFilePatternName;
	}

	/**
	 * Devuelve el char correspondiente al filesystem que permite separar los directorios
	 * 
	 * @return El char para separar directorios
	 */
	private char getDirectorySeparatorChar() {
		return File.separatorChar;
	}

	/**
	 * Obtiene el nombre de archivo historico agregando el patron de fecha en el medio, antes de la
	 * extension
	 * 
	 * @param baseFile
	 *            Archivo usado de base
	 */
	private String calculateHistoricNamePattern(final File baseFile) {
		final String completeBaseName = baseFile.getName();
		final int extensionPoint = completeBaseName.lastIndexOf('.');
		String fileNamePreffix;
		String fileNameExtension;
		if (extensionPoint < 0) {
			fileNamePreffix = completeBaseName;
			fileNameExtension = "";
		} else {
			fileNamePreffix = completeBaseName.substring(0, extensionPoint);
			fileNameExtension = completeBaseName.substring(extensionPoint);
		}

		final StringBuilder historicNameBuilder = new StringBuilder();
		historicNameBuilder.append(fileNamePreffix);
		historicNameBuilder.append(".%d{yyyy-MM-dd}");
		historicNameBuilder.append(fileNameExtension);
		return historicNameBuilder.toString();
	}

	/**
	 * Crea el directorio pasado si no existe
	 * 
	 * @param baseDirectory
	 *            Directorio a comprobar y crear si no existe
	 */
	private void ensureDirectory(final File baseDirectory) {
		if (!baseDirectory.exists()) {
			final boolean couldMakeDirs = baseDirectory.mkdirs();
			if (!couldMakeDirs) {
				throw new UnhandledConditionException("No se pudo crear los directorios para llegar a: "
						+ baseDirectory);
			}
		}
		if (!baseDirectory.isDirectory()) {
			throw new UnhandledConditionException("El file contenedor de logs no es un directorio?: " + baseDirectory);
		}
	}

	/**
	 * Agrega el appender indicado en el logger pasado
	 * 
	 * @param baseLogger
	 *            El logger que incluira el appender (y todos sus hijos)
	 * @param appender
	 *            El appender agregado
	 * @param appenderName
	 *            El nombre asignado al appender
	 */
	private void addAppenderTo(final Logger baseLogger, final Appender<ILoggingEvent> appender,
			final String appenderName) {
		final LoggerContext loggerContext = baseLogger.getLoggerContext();
		appender.setContext(loggerContext);
		appender.setName(appenderName);
		baseLogger.addAppender(appender);
	}

	/**
	 * Establece el nivel default de logueo a INFO
	 */
	public void setInfoAsDefault() {
		setRootLevel(Level.INFO);
	}

	/**
	 * Establece el nivel de logueo para root
	 * 
	 * @param newLevel
	 *            El nivel de logueo
	 */
	private void setRootLevel(final Level newLevel) {
		final Logger rootLogger = getRootLogger();
		rootLogger.setLevel(newLevel);
	}

	/**
	 * Establece el nivel default de logueo a DEBUG
	 */
	public void setDebugAsDefault() {
		setRootLevel(Level.DEBUG);
	}

	/**
	 * Establece el nivel de logueo a debug para la categoría indicada
	 * 
	 * @param categoryName
	 *            nombre que identifica la categoría a utilizar con nivel info
	 */
	@Deprecated
	public void setDebugFor(final String categoryName) {
		final Logger logger = getLoggerNamed(categoryName);
		logger.setLevel(Level.DEBUG);
	}

}

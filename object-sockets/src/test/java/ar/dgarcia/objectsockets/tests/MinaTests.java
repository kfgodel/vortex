/**
 * Created on: 30/10/2010 19:51:54 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package ar.dgarcia.objectsockets.tests;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Test;

/**
 * Esta clase prueba el funcionamiento de mina a través de sockets TCP
 * 
 * @author D. Garcia
 */
public class MinaTests {

	/**
	 * 
	 */
	private static final int TEST_PORT = 46660;

	/**
	 * Prueba el código mínimo para establecer una conexion
	 */
	@Test
	public void testSocketConnection() throws IOException, InterruptedException {
		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final IoHandlerAdapter dummyHandler = new IoHandlerAdapter() {
		};
		nioSocketAcceptor.setHandler(dummyHandler);
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);
		nioSocketAcceptor.bind(testAddress);

		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(dummyHandler);
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);
		final boolean connected = connectTask.await(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería estar conectado", connected);

		nioSocketConnector.dispose(true);
		nioSocketAcceptor.dispose(true);
	}

	public static class CounterHandler extends IoHandlerAdapter {
		private int openedSessions = 0;
		private int closedSessions = 0;
		private int createdSessions = 0;

		public int getOpenedSessions() {
			return openedSessions;
		}

		public int getClosedSessions() {
			return closedSessions;
		}

		public int getCreatedSessions() {
			return createdSessions;
		}

		
		public void sessionOpened(final IoSession session) throws Exception {
			openedSessions++;
		}

		
		public void sessionClosed(final IoSession session) throws Exception {
			closedSessions++;
		}

		
		public void sessionCreated(final IoSession session) throws Exception {
			createdSessions++;
		}
	}

	@Test
	public void testHandlerEvents() throws IOException, InterruptedException {
		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);
		final CounterHandler counterHandler = new CounterHandler();
		nioSocketAcceptor.setHandler(counterHandler);

		Assert.assertEquals("No debería tener ninguna sesion", 0, counterHandler.getCreatedSessions());
		Assert.assertEquals("No debería tener ninguna sesion", 0, counterHandler.getOpenedSessions());
		Assert.assertEquals("No debería tener ninguna sesion", 0, counterHandler.getClosedSessions());

		nioSocketAcceptor.bind(testAddress);

		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(counterHandler);
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);
		final boolean connected = connectTask.await(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería estar conectado", connected);

		nioSocketConnector.dispose(true);
		nioSocketAcceptor.dispose(true);

		Assert.assertEquals("Debería tener sesiones creadas", 2, counterHandler.getCreatedSessions());
		Assert.assertEquals("Debería tener sesiones abiertas", 2, counterHandler.getOpenedSessions());
		Assert.assertEquals("Debería tener sesiones cerradas", 2, counterHandler.getClosedSessions());
	}

	@Test
	public void testSerializedConnection() throws IOException, InterruptedException {
		final Semaphore semaphore = new Semaphore(0);
		final AtomicReference<Object> received = new AtomicReference<Object>(null);
		final IoHandlerAdapter dummyHandler = new IoHandlerAdapter() {
			
			public void messageReceived(final IoSession session, final Object message) throws Exception {
				received.set(message);
				semaphore.release();
			}
		};

		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);
		nioSocketAcceptor.setHandler(dummyHandler);

		// Agregamos el transformador a serializado
		nioSocketAcceptor.getFilterChain().addLast("serializer",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		nioSocketAcceptor.bind(testAddress);

		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(dummyHandler);

		// Agregamos el transformador a serializado
		nioSocketConnector.getFilterChain().addLast("serializer",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);

		final boolean connected = connectTask.await(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería estar conectado", connected);

		Assert.assertNull("no debería tener ningun mensaje", received.get());
		final IoSession connectorSession = connectTask.getSession();
		final String sendedMessage = "Hola Mundo!";
		connectorSession.write(sendedMessage);

		final boolean acquired = semaphore.tryAcquire(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería haber llegado el mensaje", acquired);
		final Object receivedMessage = received.get();
		Assert.assertEquals("Debería ser el mismo mensaje", sendedMessage, receivedMessage);

		nioSocketConnector.dispose(true);
		nioSocketAcceptor.dispose(true);

	}

	@Test
	public void testDisconnectionWhileConnecting() throws IOException, InterruptedException {
		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);

		final Semaphore openBlockingSemaphore = new Semaphore(0);
		final AtomicBoolean sessionOpenProcessed = new AtomicBoolean(false);
		final AtomicBoolean sessionCloseProcessedBeforeOpen = new AtomicBoolean(false);
		final IoHandlerAdapter blockedHandler = new IoHandlerAdapter() {
			
			public void sessionOpened(final IoSession session) throws Exception {
				openBlockingSemaphore.tryAcquire(3, TimeUnit.MINUTES);
				sessionOpenProcessed.set(true);
			}

			
			public void sessionClosed(final IoSession session) throws Exception {
				final boolean alreadyOpened = sessionOpenProcessed.get();
				if (!alreadyOpened) {
					sessionCloseProcessedBeforeOpen.set(true);
				}
			}
		};
		nioSocketAcceptor.setHandler(blockedHandler);
		nioSocketAcceptor.bind(testAddress);

		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(new IoHandlerAdapter() {
		});
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);
		final boolean connected = connectTask.await(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería estar conectado", connected);

		// Desconectamos antes de liberar el semáforo
		nioSocketConnector.dispose(true);

		// Una vez desconectados liberamos el semáforo para que se procese todo
		openBlockingSemaphore.release();
		nioSocketAcceptor.dispose(true);

		Assert.assertTrue("Debería estar procesado el open", sessionOpenProcessed.get());
		Assert.assertFalse("Debería procesarse el open antes que el close", sessionCloseProcessedBeforeOpen.get());
	}

	@Test
	public void testConnectionSuccessListener() throws IOException, InterruptedException {
		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);
		final IoHandlerAdapter dummyHandler = new IoHandlerAdapter() {
		};
		nioSocketAcceptor.setHandler(dummyHandler);
		nioSocketAcceptor.bind(testAddress);

		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(dummyHandler);
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);

		final AtomicBoolean connectSuccessful = new AtomicBoolean(false);
		final Semaphore semaphore = new Semaphore(0);
		connectTask.addListener(new IoFutureListener<IoFuture>() {
			
			public void operationComplete(final IoFuture future) {
				final ConnectFuture connectionTask = (ConnectFuture) future;
				connectSuccessful.set(connectionTask.isConnected());
				semaphore.release();
			}
		});

		semaphore.acquire();
		Assert.assertTrue("Debería estar conectado", connectSuccessful.get());

		nioSocketConnector.dispose(true);
		nioSocketAcceptor.dispose(true);
	}

	@Test
	public void testConnectionFailureListener() throws IOException, InterruptedException {
		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		final IoHandlerAdapter dummyHandler = new IoHandlerAdapter() {
		};
		nioSocketConnector.setHandler(dummyHandler);

		// Dirección sin nadie que escuche
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);

		final AtomicBoolean connectSuccessful = new AtomicBoolean(false);
		final Semaphore semaphore = new Semaphore(0);
		connectTask.addListener(new IoFutureListener<IoFuture>() {
			
			public void operationComplete(final IoFuture future) {
				final ConnectFuture connectionTask = (ConnectFuture) future;
				connectSuccessful.set(connectionTask.isConnected());
				semaphore.release();
			}
		});

		semaphore.acquire();
		Assert.assertFalse("Debería estar desconectado", connectSuccessful.get());

		nioSocketConnector.dispose(true);
	}

	/**
	 * Probamos si un handler puede recibir un mensaje en la misma sesion sin haber termina de
	 * procesar el anterior<br>
	 * Este test fue diseñado con la idea de que era posible, pero comprobé que no. Hasta que no
	 * termina de procesar la respuesta, no se envia el segundo mensajes (a pesar de haber llamado
	 * antes a session.write()). Con lo que como estaba escrito el test se bloqueaba.<br>
	 * Si alguna vez cambia eso o si se duda de eso, sólo descomentar la linea comentada
	 */
	@Test
	public void testResponseBeforeQuestionEnd() throws IOException, InterruptedException {
		final String firstMessage = "ONE";
		final String firstResponse = "TWO";
		final String secondMessage = "THREE";
		final String secondResponse = "FOUR";

		// Creamos el server que nos envia las respuestas a los mensajes
		final NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		final InetSocketAddress testAddress = new InetSocketAddress(TEST_PORT);

		final Semaphore secondResponseSent = new Semaphore(0);
		final IoHandlerAdapter responseHandler = new IoHandlerAdapter() {
			
			public void messageReceived(final IoSession session, final Object message) throws Exception {
				if (firstMessage.equals(message)) {
					session.write(firstResponse);
				}
				if (secondMessage.equals(message)) {
					session.write(secondResponse);
				}
			}

			/**
			 * @see org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina.core.session.IoSession,
			 *      java.lang.Object)
			 */
			
			public void messageSent(final IoSession session, final Object message) throws Exception {
				if (secondResponse.equals(message)) {
					secondResponseSent.release();
				}
			}
		};
		nioSocketAcceptor.setHandler(responseHandler);
		nioSocketAcceptor.getFilterChain().addLast("binary2string",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		nioSocketAcceptor.bind(testAddress);

		// Flags para verificar si se proceso la respuesta antes que el envio de la pregunta
		final AtomicBoolean secondResponseProcessedBeforeFirst = new AtomicBoolean(false);
		final AtomicBoolean firstResponseProcessed = new AtomicBoolean(false);
		final Semaphore firstMessageProcess = new Semaphore(0);
		final Semaphore secondResponseProcessed = new Semaphore(0);

		// Creamos el emisor que se bloquea despues de
		final IoHandler testHandler = new IoHandlerAdapter() {
			
			public void messageReceived(final IoSession session, final Object message) throws Exception {
				if (firstResponse.equals(message)) {
					session.write(secondMessage);
					// Bloquamos el hilo como si estuviera activo procesando algo, después de haber
					// enviado respuesta.
					// Descomentar si se duda de recibir más de un mensaje a la vez!
					// firstMessageProcess.acquire();
					firstResponseProcessed.set(true);
				}
				if (secondResponse.equals(message)) {
					// Llego la segunda respuesta, vemos si antes de terminar la anterior
					if (!firstResponseProcessed.get()) {
						secondResponseProcessedBeforeFirst.set(true);
					}
					secondResponseProcessed.release();
				}
			}
		};
		final NioSocketConnector nioSocketConnector = new NioSocketConnector();
		nioSocketConnector.setHandler(testHandler);
		nioSocketConnector.getFilterChain().addLast("binary2string",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		final ConnectFuture connectTask = nioSocketConnector.connect(testAddress);
		final boolean connected = connectTask.await(5, TimeUnit.SECONDS);
		Assert.assertTrue("Debería estar conectado", connected);

		// Enviamos el primer mensaje que genera todo
		final IoSession session = connectTask.getSession();
		session.write(firstMessage);

		// Damos tiempo para que la segunda respuesta llegue despues de ser enviada
		secondResponseSent.acquire();
		Thread.sleep(1000);
		// Permitimos que el proceso de la primera respuesta termine
		firstMessageProcess.release();

		// Esperamos que la segunda respuesta sea procesada
		secondResponseProcessed.release();

		Assert.assertFalse("La primera respuesta debería procesar antes que el segundo",
				secondResponseProcessedBeforeFirst.get());

		nioSocketConnector.dispose(true);
		nioSocketAcceptor.dispose(true);
	}
}

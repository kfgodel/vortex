/**
 * Created on 11/01/2007 21:37:52
 * Copyright (C) 2007  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.comm.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es la implementacion default de un {@link ActionRepertoire}
 * 
 * @version 1.0
 * @since 11/01/2007
 * @author D. Garcia 
 */
public class DefaultActionRepertoire implements ActionRepertoire{

	/**
	 * Acciones a realizar por cada tipo de mensaje
	 */
	private Map<Class<?>,MessageHandler<?>> handlers = new HashMap<Class<?>,MessageHandler<?>>();
	/**
	 * @see ar.com.dgarcia.comm.handlers.ActionRepertoire#doFor(java.lang.Class, ar.com.dgarcia.comm.handlers.MessageHandler)
	 */
	public <T> void doFor(Class<T> messageType, MessageHandler<T> typeHandler) {
		handlers.put(messageType, typeHandler);
	}

	/**
	 * @see ar.com.dgarcia.comm.handlers.ActionRepertoire#getHandlerFor(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> MessageHandler<T> getHandlerFor(Class<? extends T> messageType) {
		MessageHandler<T> handler = (MessageHandler<T>) handlers.get(messageType);
		return handler;
	}

}

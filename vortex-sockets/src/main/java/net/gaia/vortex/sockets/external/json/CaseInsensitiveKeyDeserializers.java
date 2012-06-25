package net.gaia.vortex.sockets.external.json;

import java.io.IOException;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;

public class CaseInsensitiveKeyDeserializers implements KeyDeserializers {
	public static final CaseInsensitiveKeyDeserializer DESERIALIZER = new CaseInsensitiveKeyDeserializer();

	private static class CaseInsensitiveKeyDeserializer extends KeyDeserializer {
		@Override
		public Object deserializeKey(final String key, final DeserializationContext ctxt) throws IOException {
			return key.toLowerCase();
		}
	}

	/**
	 * @see com.fasterxml.jackson.databind.deser.KeyDeserializers#findKeyDeserializer(com.fasterxml.jackson.databind.JavaType,
	 *      com.fasterxml.jackson.databind.DeserializationConfig,
	 *      com.fasterxml.jackson.databind.BeanDescription)
	 */
	@Override
	public KeyDeserializer findKeyDeserializer(final JavaType type, final DeserializationConfig config,
			final BeanDescription beanDesc) throws JsonMappingException {
		return DESERIALIZER;
	}
}
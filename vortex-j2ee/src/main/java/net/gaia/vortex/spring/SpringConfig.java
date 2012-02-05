package net.gaia.vortex.spring;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.impl.InterpreteJackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Esta clase representa la configuración programática de Spring en la que se definen los
 * componentes de la aplicación. Los métodos sirven de definiciones de componentes, y los
 * annotations indican como se interpretan
 * 
 * @author D. García
 */
@Configuration
@ImportResource({ "classpath:spring/cron-context.xml" })
public class SpringConfig {

	@Bean
	public InterpreteJson getInterpreteJson() {
		return InterpreteJackson.create();
	}

}

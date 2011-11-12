package net.gaia.vortex.spring;

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
@ImportResource({ "classpath:spring/persistence-context.xml", "classpath:spring/cron-context.xml" })
public class SpringConfig {

}

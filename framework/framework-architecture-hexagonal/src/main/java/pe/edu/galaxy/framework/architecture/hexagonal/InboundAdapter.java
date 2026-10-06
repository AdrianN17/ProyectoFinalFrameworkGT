package pe.edu.galaxy.framework.architecture.hexagonal;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adaptador de entrada (driving adapter): traduce una tecnologia de entrada (REST, mensajeria,
 * CLI) hacia los casos de uso de la aplicacion.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface InboundAdapter {
}

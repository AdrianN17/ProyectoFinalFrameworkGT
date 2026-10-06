package pe.edu.galaxy.framework.architecture.hexagonal;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adaptador de salida (driven adapter): implementa un {@link OutboundPort} usando una tecnologia
 * concreta (JPA, cliente HTTP, cola de mensajes).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OutboundAdapter {
}

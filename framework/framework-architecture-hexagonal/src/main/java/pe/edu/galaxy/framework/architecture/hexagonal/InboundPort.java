package pe.edu.galaxy.framework.architecture.hexagonal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca una interfaz como puerto de entrada (driving port): contrato con el que el mundo exterior
 * invoca a la aplicacion. No es un bean de Spring.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InboundPort {
}

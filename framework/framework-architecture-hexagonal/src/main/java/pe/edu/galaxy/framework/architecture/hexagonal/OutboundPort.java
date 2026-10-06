package pe.edu.galaxy.framework.architecture.hexagonal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca una interfaz como puerto de salida (driven port): contrato que el dominio o la aplicacion
 * necesitan de la infraestructura (persistencia, servicios externos). Lo implementa un
 * {@link OutboundAdapter}. No es un bean de Spring.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OutboundPort {
}

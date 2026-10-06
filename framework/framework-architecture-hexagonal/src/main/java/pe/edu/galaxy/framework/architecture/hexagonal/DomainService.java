package pe.edu.galaxy.framework.architecture.hexagonal;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Servicio de dominio: logica de negocio que no pertenece a una sola entidad. Solo depende del
 * propio dominio y de sus puertos.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DomainService {
}

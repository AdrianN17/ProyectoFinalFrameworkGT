package pe.edu.galaxy.framework.architecture.hexagonal;

import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implementacion de un caso de uso (capa de aplicacion). Orquesta el dominio a traves de puertos
 * y no conoce detalles de infraestructura.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ApplicationService {
}

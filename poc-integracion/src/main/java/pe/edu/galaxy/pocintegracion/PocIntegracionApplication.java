package pe.edu.galaxy.pocintegracion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import pe.edu.galaxy.pocintegracion.generated.server.OpenApiGeneratorApplication;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OpenApiGeneratorApplication.class))
public class PocIntegracionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocIntegracionApplication.class, args);
    }
}

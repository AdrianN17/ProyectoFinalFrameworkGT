package pe.edu.galaxy.finalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import pe.edu.galaxy.finalapp.generated.server.OpenApiGeneratorApplication;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OpenApiGeneratorApplication.class))
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }
}

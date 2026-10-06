package pe.edu.galaxy.pocintegracion;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/** Verifica las reglas de dependencia de la arquitectura hexagonal. */
class HexagonalArchitectureTest {

    private static final String BASE = "pe.edu.galaxy.pocintegracion";

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE);
    }

    @Test
    void domainDoesNotDependOnApplicationOrAdapters() {
        noClasses().that().resideInAPackage(BASE + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(BASE + ".application..", BASE + ".adapter..", BASE + ".generated..")
                .check(classes);
    }

    @Test
    void domainIsFrameworkAgnostic() {
        noClasses().that().resideInAPackage(BASE + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..", "jakarta.persistence..", "pe.edu.galaxy.framework.jpa..",
                        "pe.edu.galaxy.framework.openapi..")
                .check(classes);
    }

    @Test
    void applicationDoesNotDependOnAdapters() {
        noClasses().that().resideInAPackage(BASE + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(BASE + ".adapter..", BASE + ".generated..", "jakarta.persistence..")
                .check(classes);
    }

    @Test
    void adaptersDoNotDependOnEachOther() {
        noClasses().that().resideInAPackage(BASE + ".adapter.in..")
                .should().dependOnClassesThat().resideInAPackage(BASE + ".adapter.out..")
                .check(classes);
        noClasses().that().resideInAPackage(BASE + ".adapter.out..")
                .should().dependOnClassesThat().resideInAPackage(BASE + ".adapter.in..")
                .check(classes);
    }

    @Test
    void inboundAdaptersDoNotAccessPersistenceDirectly() {
        noClasses().that().resideInAPackage(BASE + ".adapter.in..")
                .should().dependOnClassesThat().resideInAnyPackage(BASE + ".domain.port..", "jakarta.persistence..")
                .check(classes);
    }

    @Test
    void outboundPortsAreInterfaces() {
        classes().that().resideInAPackage(BASE + ".domain.port.out..")
                .and().areTopLevelClasses()
                .should().beInterfaces()
                .check(classes);
    }

    @Test
    void outboundPortsAreImplementedOnlyByOutboundAdapters() {
        classes().that().implement(com.tngtech.archunit.base.DescribedPredicate.describe(
                        "an outbound port",
                        c -> c.getPackageName().startsWith(BASE + ".domain.port.out")))
                .should().resideInAPackage(BASE + ".adapter.out..")
                .check(classes);
    }
}

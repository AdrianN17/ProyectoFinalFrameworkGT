package pe.edu.galaxy.framework.architecture.hexagonal;

import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

class HexagonalAnnotationsTest {

    @Test
    void applicationServiceIsSpringService() {
        assertThat(AnnotatedElementUtils.hasAnnotation(ApplicationService.class, Service.class)).isTrue();
    }

    @Test
    void domainServiceAndAdaptersAreSpringComponents() {
        assertThat(AnnotatedElementUtils.hasAnnotation(DomainService.class, Component.class)).isTrue();
        assertThat(AnnotatedElementUtils.hasAnnotation(InboundAdapter.class, Component.class)).isTrue();
        assertThat(AnnotatedElementUtils.hasAnnotation(OutboundAdapter.class, Component.class)).isTrue();
    }

    @Test
    void portsAreMarkersAndNeverSpringBeans() {
        assertThat(AnnotatedElementUtils.hasAnnotation(InboundPort.class, Component.class)).isFalse();
        assertThat(AnnotatedElementUtils.hasAnnotation(OutboundPort.class, Component.class)).isFalse();
    }
}

package pe.edu.galaxy.pocintegracion.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCardAccount, Long> {

    Optional<CreditCardAccount> findByExternalId(String externalId);
}

package api.desafio.contaazul.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import api.desafio.contaazul.entitys.BankSlipEntity;

/**
 * @author gscarabelo
 * @since 10/30/18 11:22 PM
 */
public interface BankSlipRepository extends CrudRepository<BankSlipEntity, Long> {

    Optional<BankSlipEntity> findById(final UUID uuid);

}
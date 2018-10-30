package api.desafio.contaazul.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import api.desafio.contaazul.dto.BankSlipFullDetailsDTO;
import api.desafio.contaazul.dto.InsertedBankSlipDTO;
import api.desafio.contaazul.dto.NewBankSlipDTO;
import api.desafio.contaazul.dto.PaymentDataDTO;
import api.desafio.contaazul.entitys.BankSlipEntity;
import api.desafio.contaazul.enums.BankSlipStatusEnum;
import api.desafio.contaazul.exceptions.BankSlipNotFoundException;
import api.desafio.contaazul.exceptions.BankSlipNotProvidedException;
import api.desafio.contaazul.exceptions.InvalidBankSlipException;
import api.desafio.contaazul.repository.BankSlipRepository;
import api.desafio.contaazul.services.BankSlipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gscarabelo
 * @since 10/30/18 12:32 PM
 */
@Service
@Slf4j
public class BankSlipServiceImpl implements BankSlipService {

    @Autowired
    BankSlipRepository bankSlipRepository;

    @Override
    public InsertedBankSlipDTO validateAndInsertNewBankSlip (final NewBankSlipDTO newBankSlip) {
        log.info("I=Inserting newBankSlip={}", newBankSlip);
        validateNewBankSlip(newBankSlip);
        BankSlipEntity bankSlipEntity = new BankSlipEntity(UUID.randomUUID(), newBankSlip.getDue_date(),
                newBankSlip.getTotal_in_cents(), newBankSlip.getCustomer(),
                BankSlipStatusEnum.PENDING);
        log.info("I=Inserting on bank, bankSlipEntity={}", bankSlipEntity);
        bankSlipRepository.save(bankSlipEntity);
        return new InsertedBankSlipDTO(bankSlipEntity);
    }

    @Override
    public List<BankSlipFullDetailsDTO> listAllBankSlips () {
        log.info("I=Listing all bank slips");
        return listOfEntitysToVo((List<BankSlipEntity>) bankSlipRepository.findAll());
    }

    @Override
    public BankSlipFullDetailsDTO getBankSlipDetails (final UUID id) {
        log.info("I=Getting bank slip with deteils by, id={}", id);
        BankSlipEntity bankSlipEntity = getBankSlipById(id);
        BankSlipFullDetailsDTO bankSlip = new BankSlipFullDetailsDTO(bankSlipEntity);
        bankSlip.setFine(
                calculateFine(bankSlip.getDue_date(), bankSlip.getPayment_date(), bankSlip.getTotal_in_cents()));
        return bankSlip;
    }

    @Override
    public void payBankSlip (final UUID id, final PaymentDataDTO paymentDate) {
        log.info("I=Paing one bank slip, id={}", id);
        BankSlipEntity bankSlipEntity = getBankSlipById(id);
        bankSlipEntity.setStatus(BankSlipStatusEnum.PAID);
        log.info("I=Verifing if bank slip already have payment date, bankSlipEntity={}", bankSlipEntity);
        if (bankSlipEntity.getPayment_date() == null) {
            bankSlipEntity.setPayment_date(paymentDate.getPayment_date());
            bankSlipRepository.save(bankSlipEntity);
        }
    }

    @Override
    public void cancelPaymentSlip (final UUID id) {
        log.info("I=Cancelling bank slip, id={}", id);
        BankSlipEntity bankSlipEntity = getBankSlipById(id);
        bankSlipEntity.setStatus(BankSlipStatusEnum.CANCELED);
        bankSlipRepository.save(bankSlipEntity);
    }

    //#Private#
    public List<BankSlipFullDetailsDTO> listOfEntitysToVo (List<BankSlipEntity> bankSlipEntities) {
        log.info("I=Converting list of entity to dto, bankSlipEntities={}", bankSlipEntities);
        List<BankSlipFullDetailsDTO> bankSlipFullDetailsDTOS = new ArrayList<>();
        bankSlipEntities.forEach(e -> {
            BankSlipFullDetailsDTO bankSlipDTO = new BankSlipFullDetailsDTO();
            bankSlipDTO.setId(e.getId());
            bankSlipDTO.setDue_date(e.getDue_date());
            bankSlipDTO.setPayment_date(e.getPayment_date());
            bankSlipDTO.setTotal_in_cents(e.getTotal_in_cents());
            bankSlipDTO.setCustomer(e.getCustomer());
            bankSlipDTO.setStatus(e.getStatus());
            bankSlipDTO.setFine(calculateFine(e.getDue_date(), e.getPayment_date(), e.getTotal_in_cents()));
            bankSlipFullDetailsDTOS.add(bankSlipDTO);
        });

        return bankSlipFullDetailsDTOS;
    }

    private BigDecimal calculateFine (final Date due_date, final Date payment_date,
            final BigDecimal total_in_cents) {
        log.info("I=Calculating fine value, due_date={}, payment_date={}", due_date, payment_date);
        if (due_date == null || payment_date == null) {
            return null;
        }
        if (TimeUnit.MILLISECONDS.toDays((payment_date.getTime() - due_date.getTime())) < 10) {
            return total_in_cents.multiply(new BigDecimal("0.005")).setScale(0, RoundingMode.UP);
        } else {
            return total_in_cents.multiply(new BigDecimal("0.01")).setScale(0, RoundingMode.UP);
        }
    }

    private BankSlipEntity getBankSlipById (final UUID id) {
        log.info("I=Getting bank splip, id={}", id);
        Optional<BankSlipEntity> bankSlipEntity = bankSlipRepository.findById(id);
        if (bankSlipEntity.isPresent()) {
            return bankSlipEntity.get();
        } else {
            throw new BankSlipNotFoundException();
        }
    }

    private void validateNewBankSlip (final NewBankSlipDTO newBankSlip) {
        log.info("I=Validating newBankSlip={}", newBankSlip);
        if (newBankSlip == null) {
            log.error("E=Bank slip not provided, invalid request, newBankSlip=null");
            throw new BankSlipNotProvidedException();
        }
        if (newBankSlip.getDue_date() == null || newBankSlip.getTotal_in_cents() == null
                || newBankSlip.getDue_date() == null) {
            log.error("E=Invalid bank slip, newBankSlip={}", newBankSlip);
            throw new InvalidBankSlipException();
        }
    }
}
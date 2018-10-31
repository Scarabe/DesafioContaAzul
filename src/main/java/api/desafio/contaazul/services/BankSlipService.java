package api.desafio.contaazul.services;

import java.util.List;
import java.util.UUID;

import api.desafio.contaazul.dto.BankSlipFullDetailsDTO;
import api.desafio.contaazul.dto.InsertedBankSlipDTO;
import api.desafio.contaazul.dto.NewBankSlipDTO;
import api.desafio.contaazul.dto.PaymentDataDTO;

/**
 * @author gscarabelo
 * @since 10/30/18 11:35 AM
 */
public interface BankSlipService {

    InsertedBankSlipDTO validateAndInsertNewBankSlip(final NewBankSlipDTO newBankSlip);

    List<BankSlipFullDetailsDTO> listAllBankSlips();

    BankSlipFullDetailsDTO getBankSlipDetails(final UUID id);

    void payBankSlip(final UUID id, final PaymentDataDTO paymentDate);

    void cancelPaymentSlip(final UUID id);
}

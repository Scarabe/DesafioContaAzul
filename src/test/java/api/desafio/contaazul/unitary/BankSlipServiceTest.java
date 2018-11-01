package api.desafio.contaazul.unitary;

import static java.util.Optional.ofNullable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import api.desafio.contaazul.dto.BankSlipFullDetailsDTO;
import api.desafio.contaazul.dto.NewBankSlipDTO;
import api.desafio.contaazul.dto.PaymentDataDTO;
import api.desafio.contaazul.entitys.BankSlipEntity;
import api.desafio.contaazul.exceptions.BankSlipNotFoundException;
import api.desafio.contaazul.exceptions.BankSlipNotProvidedException;
import api.desafio.contaazul.exceptions.InvalidBankSlipException;
import api.desafio.contaazul.repository.BankSlipRepository;
import api.desafio.contaazul.services.BankSlipService;
import api.desafio.contaazul.services.impl.BankSlipServiceImpl;

/**
 * @author gscarabelo
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 10/31/18 12:02 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class BankSlipServiceTest {

    @Mock
    private BankSlipRepository bankSlipRepository;

    @InjectMocks
    private BankSlipService bankSlipService = new BankSlipServiceImpl();

    private NewBankSlipDTO newBankSlipDTO = new NewBankSlipDTO();

    private BankSlipEntity bankSlipEntity = new BankSlipEntity();

    private UUID id;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();

        newBankSlipDTO.setCustomer("Customer test");
        newBankSlipDTO.setDue_date(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        newBankSlipDTO.setTotal_in_cents(new BigDecimal(250).setScale(2));

        bankSlipEntity.setId(id);
        bankSlipEntity.setDue_date(newBankSlipDTO.getDue_date());
        bankSlipEntity.setTotal_in_cents(newBankSlipDTO.getTotal_in_cents());
        bankSlipEntity.setTotal_in_cents(newBankSlipDTO.getTotal_in_cents());
        bankSlipEntity.setCustomer(newBankSlipDTO.getCustomer());
    }

    @Test
    public void whenListBankLisp() {
        List<BankSlipEntity> bankSlipEntities = new ArrayList<>();
        bankSlipEntities.add(bankSlipEntity);
        Mockito.when(bankSlipRepository.findAll()).thenReturn(bankSlipEntities);
        Assert.assertEquals(bankSlipService.listAllBankSlips().get(0).getClass(), BankSlipFullDetailsDTO.class);
    }

    @Test
    public void whenDetailsHasAllData() {
        Mockito.when(bankSlipRepository.findById(id)).thenReturn(ofNullable(bankSlipEntity));
        Assert.assertEquals(bankSlipService.getBankSlipDetails(id), new BankSlipFullDetailsDTO(bankSlipEntity));
    }

    @Test
    public void whenDatailsHasPaymentDateBeforeTenDays() throws ParseException {
        bankSlipEntity.setPayment_date(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-05"));
        bankSlipEntity.setFine(new BigDecimal(1.25).setScale(2));
        Mockito.when(bankSlipRepository.findById(id)).thenReturn(ofNullable(bankSlipEntity));
        BankSlipFullDetailsDTO bankSlipFullDetailsDTO = new BankSlipFullDetailsDTO(bankSlipEntity);
        bankSlipFullDetailsDTO.setFine(
                newBankSlipDTO.getTotal_in_cents().multiply(new BigDecimal("0.005")).setScale(2));
        Assert.assertEquals(bankSlipService.getBankSlipDetails(id), bankSlipFullDetailsDTO);
    }

    @Test
    public void whenDatailsHasPaymentDateAfterTenDays() throws ParseException {
        bankSlipEntity.setPayment_date(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-11"));
        bankSlipEntity.setFine(new BigDecimal(2.50).setScale(2));
        Mockito.when(bankSlipRepository.findById(id)).thenReturn(ofNullable(bankSlipEntity));
        BankSlipFullDetailsDTO bankSlipFullDetailsDTO = new BankSlipFullDetailsDTO(bankSlipEntity);
        bankSlipFullDetailsDTO.setFine(
                newBankSlipDTO.getTotal_in_cents().multiply(new BigDecimal("0.01")).setScale(2));
        Assert.assertEquals(bankSlipService.getBankSlipDetails(id), bankSlipFullDetailsDTO);
    }

    @Test
    public void whenCancelPaymentHasAllData() {
        Mockito.when(bankSlipRepository.findById(id)).thenReturn(ofNullable(bankSlipEntity));
        bankSlipService.cancelPaymentSlip(id);
    }

    @Test
    public void whenPaymentHasAllData() throws ParseException {
        Date payment = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-05");
        bankSlipEntity.setPayment_date(payment);
        Mockito.when(bankSlipRepository.findById(id)).thenReturn(ofNullable(bankSlipEntity));
        bankSlipService.payBankSlip(id, new PaymentDataDTO(payment));
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void whenPaymentReceiveNullData() {
        Mockito.when(bankSlipRepository.findById((Long) null)).thenReturn(Optional.empty());
        bankSlipService.payBankSlip(null, null);
    }

    @Test(expected = BankSlipNotProvidedException.class)
    public void wheNullBodyOnInsertionRequest() {
        bankSlipService.validateAndInsertNewBankSlip(null);
    }

    @Test(expected = InvalidBankSlipException.class)
    public void whenNullCustomer() {
        newBankSlipDTO.setCustomer(null);
        bankSlipService.validateAndInsertNewBankSlip(newBankSlipDTO);
    }

    @Test(expected = InvalidBankSlipException.class)
    public void whenNullDueDate() {
        newBankSlipDTO.setDue_date(null);
        bankSlipService.validateAndInsertNewBankSlip(newBankSlipDTO);
    }

    @Test(expected = InvalidBankSlipException.class)
    public void whenNullTotalInCents() {
        newBankSlipDTO.setTotal_in_cents(null);
        bankSlipService.validateAndInsertNewBankSlip(newBankSlipDTO);
    }

}

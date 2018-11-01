package api.desafio.contaazul.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import api.desafio.contaazul.dto.BankSlipFullDetailsDTO;
import api.desafio.contaazul.dto.InsertedBankSlipDTO;
import api.desafio.contaazul.dto.NewBankSlipDTO;
import api.desafio.contaazul.dto.PaymentDataDTO;
import api.desafio.contaazul.services.BankSlipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gscarabelo
 * @since 10/30/18 10:46 PM
 */
@RestController
@Slf4j
@RequestMapping("/rest")
@Api(value = "Bank slip", description = "Only for bank slip operations")
public class BankSlipController {

    @Autowired
    private BankSlipService bankSlipService;

    @ApiOperation(value = "Insert new bank slip")
    @RequestMapping(value = "/bankslips", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<InsertedBankSlipDTO> insertNewBankSlip(
            @RequestBody(required = false) final NewBankSlipDTO newBankSlip) {
        log.info("I=Inserting new bank slip");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankSlipService.validateAndInsertNewBankSlip(newBankSlip));
    }

    @ApiOperation(value = "List all bank slips")
    @RequestMapping(value = "/bankslips/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BankSlipFullDetailsDTO>> listBankSlips() {
        log.info("I=Returning all bank slips");
        return ResponseEntity.status(HttpStatus.OK)
                .body(bankSlipService.listAllBankSlips());
    }

    @ApiOperation(value = "Get a single bank slip with details")
    @RequestMapping(value = "/bankslips/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BankSlipFullDetailsDTO> getBankSlipDetails(@PathVariable(value = "id") final UUID id) {
        log.info("I=Returning details of a bankSlip, id={}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(bankSlipService.getBankSlipDetails(id));
    }

    @ApiOperation(value = "Pay a bank slip")
    @RequestMapping(value = "/bankslips/{id}/payments", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity bankSplipPayment(@PathVariable(value = "id") final UUID id,
            @RequestBody final PaymentDataDTO payment_date) {
        log.info("I=Returning all bank slips");
        bankSlipService.payBankSlip(id, payment_date);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Cancel one bank slip")
    @RequestMapping(value = "/bankslips/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteBankSlip(@PathVariable(value = "id") final UUID id) {
        log.info("I=Returning all bank slips");
        bankSlipService.cancelPaymentSlip(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bankslip canceled");
    }
}
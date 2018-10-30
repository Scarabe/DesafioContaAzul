package api.desafio.contaazul.exceptions;

import javax.validation.Payload;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gscarabelo
 * @since 10/30/18 12:46 PM
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankSlipNotFoundException extends RuntimeException implements Payload {

    public BankSlipNotFoundException () {
        super("Bankslip not found with the specified id");
    }

}

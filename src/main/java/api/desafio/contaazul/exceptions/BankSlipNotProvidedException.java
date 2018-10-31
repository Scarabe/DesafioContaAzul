package api.desafio.contaazul.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gscarabelo
 * @since 10/30/18 12:46 PM
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankSlipNotProvidedException extends RuntimeException {

    public BankSlipNotProvidedException() {
        super("Bankslip not provided in the request body");
    }

}

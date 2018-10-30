package api.desafio.contaazul.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gscarabelo
 * @since 10/30/18 12:46 PM
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidBankSlipException extends RuntimeException {

    public InvalidBankSlipException () {
        super("Invalid bankslip provided.The possible reasons are:"
                + " A field of the provided bankslip was null or with invalid values");
    }

}

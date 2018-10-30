package api.desafio.contaazul.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gscarabelo
 * @since 10/30/18 4:03 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDataDTO {

    private Date payment_date;
}

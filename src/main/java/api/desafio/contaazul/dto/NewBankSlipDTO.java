package api.desafio.contaazul.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;

/**
 * @author gscarabelo
 * @since 10/30/18 11:31 AM
 */
@Data
@ToString
public class NewBankSlipDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-3")
    private Date due_date;

    private BigDecimal total_in_cents;

    private String customer;
}

package api.desafio.contaazul.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import api.desafio.contaazul.entitys.BankSlipEntity;
import api.desafio.contaazul.enums.BankSlipStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gscarabelo
 * @since 10/30/18 3:28 PM
 */
@Data
@NoArgsConstructor
public class BankSlipFullDetailsDTO {

    private UUID id;

    private Date due_date;

    private Date payment_date;

    private BigDecimal total_in_cents;

    private String customer;

    private BankSlipStatusEnum status;

    private BigDecimal fine;

    public BankSlipFullDetailsDTO(BankSlipEntity bankSlipEntities) {
        this.id = bankSlipEntities.getId();
        this.due_date = bankSlipEntities.getDue_date();
        this.payment_date = bankSlipEntities.getPayment_date();
        this.total_in_cents = bankSlipEntities.getTotal_in_cents();
        this.customer = bankSlipEntities.getCustomer();
        this.status = bankSlipEntities.getStatus();
    }
}

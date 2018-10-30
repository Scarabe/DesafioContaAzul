package api.desafio.contaazul.entitys;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import api.desafio.contaazul.enums.BankSlipStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author gscarabelo
 * @since 10/30/18 11:15 AM
 */
@Entity(name = "BANK_SLIP")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BankSlipEntity {

    @Id
    @NonNull
    private UUID id;

    @NonNull
    private Date due_date;

    private Date payment_date;

    @NonNull
    private BigDecimal total_in_cents;

    @NonNull
    private String customer;

    @NonNull
    private BankSlipStatusEnum status;
}

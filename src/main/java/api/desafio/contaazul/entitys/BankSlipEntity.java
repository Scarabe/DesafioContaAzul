package api.desafio.contaazul.entitys;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @Temporal(TemporalType.DATE)
    private Date due_date;

    @Temporal(TemporalType.DATE)
    private Date payment_date;

    @NonNull
    @Column(precision = 11, scale = 0)
    private BigDecimal total_in_cents;

    @NonNull
    private String customer;

    @NonNull
    private BankSlipStatusEnum status;
}

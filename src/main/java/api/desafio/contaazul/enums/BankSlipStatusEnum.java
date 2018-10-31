package api.desafio.contaazul.enums;

/**
 * @author gscarabelo
 * @since 10/30/18 11:31 AM
 */
public enum BankSlipStatusEnum {
    PENDING("pending"), //
    PAID("paid"), //
    CANCELED("canceled");

    private final String description;

    BankSlipStatusEnum(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

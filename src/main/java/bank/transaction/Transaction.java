package bank.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private int accountId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime time;

    public Transaction(
            int accountId,
            BigDecimal amount,
            String type
    ) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.time = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
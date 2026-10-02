package bank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Account {

    private int id;
    private int customerId;

    private String deposit1Number;
    private String deposit2Number;

    private BigDecimal deposit1Balance;
    private BigDecimal deposit2Balance;

    private BigDecimal deposit1Rate;
    private int deposit1TermMonths;
    private LocalDate deposit1OpenDate;
    private LocalDate deposit1EndDate;
    private int deposit1SimulatedMonths;
    private BigDecimal deposit1Reward;

    private BigDecimal deposit2Rate;
    private int deposit2TermMonths;
    private LocalDate deposit2OpenDate;
    private LocalDate deposit2EndDate;
    private int deposit2SimulatedMonths;
    private BigDecimal deposit2Reward;

    public Account(
            int customerId,
            String deposit1Number,
            String deposit2Number
    ) {
        this.customerId = customerId;
        this.deposit1Number = deposit1Number;
        this.deposit2Number = deposit2Number;

        this.deposit1Balance = BigDecimal.ZERO;
        this.deposit2Balance = BigDecimal.ZERO;

        this.deposit1Reward = BigDecimal.ZERO;
        this.deposit2Reward = BigDecimal.ZERO;

        this.deposit1SimulatedMonths = 0;
        this.deposit2SimulatedMonths = 0;
    }

    public Account(
            int id,
            int customerId,
            String deposit1Number,
            String deposit2Number,
            BigDecimal deposit1Balance,
            BigDecimal deposit2Balance,
            BigDecimal deposit1Rate,
            int deposit1TermMonths,
            LocalDate deposit1OpenDate,
            LocalDate deposit1EndDate,
            int deposit1SimulatedMonths,
            BigDecimal deposit1Reward,
            BigDecimal deposit2Rate,
            int deposit2TermMonths,
            LocalDate deposit2OpenDate,
            LocalDate deposit2EndDate,
            int deposit2SimulatedMonths,
            BigDecimal deposit2Reward
    ) {
        this.id = id;
        this.customerId = customerId;
        this.deposit1Number = deposit1Number;
        this.deposit2Number = deposit2Number;
        this.deposit1Balance = deposit1Balance;
        this.deposit2Balance = deposit2Balance;

        this.deposit1Rate = deposit1Rate;
        this.deposit1TermMonths = deposit1TermMonths;
        this.deposit1OpenDate = deposit1OpenDate;
        this.deposit1EndDate = deposit1EndDate;
        this.deposit1SimulatedMonths = deposit1SimulatedMonths;
        this.deposit1Reward = deposit1Reward;

        this.deposit2Rate = deposit2Rate;
        this.deposit2TermMonths = deposit2TermMonths;
        this.deposit2OpenDate = deposit2OpenDate;
        this.deposit2EndDate = deposit2EndDate;
        this.deposit2SimulatedMonths = deposit2SimulatedMonths;
        this.deposit2Reward = deposit2Reward;
    }

    public boolean openFirstDeposit(
            BigDecimal rate,
            int termMonths
    ) {
        if (!isValidTerm(termMonths)) {
            return false;
        }

        this.deposit1Rate = rate;
        this.deposit1TermMonths = termMonths;
        this.deposit1OpenDate = LocalDate.now();
        this.deposit1EndDate =
                deposit1OpenDate.plusMonths(termMonths);
        this.deposit1SimulatedMonths = 0;
        this.deposit1Reward = BigDecimal.ZERO;

        return true;
    }

    public boolean openSecondDeposit(
            BigDecimal rate,
            int termMonths
    ) {
        if (!isValidTerm(termMonths)) {
            return false;
        }

        this.deposit2Rate = rate;
        this.deposit2TermMonths = termMonths;
        this.deposit2OpenDate = LocalDate.now();
        this.deposit2EndDate =
                deposit2OpenDate.plusMonths(termMonths);
        this.deposit2SimulatedMonths = 0;
        this.deposit2Reward = BigDecimal.ZERO;

        return true;
    }

    private boolean isValidTerm(int termMonths) {
        return termMonths == 3
                || termMonths == 6
                || termMonths == 9
                || termMonths == 12;
    }

    public BigDecimal simulateFirstDepositMonth() {

        if (deposit1Rate == null) {
            return BigDecimal.ZERO;
        }

        if (deposit1SimulatedMonths >= deposit1TermMonths) {
            return BigDecimal.ZERO;
        }

        BigDecimal monthlyRate =
                deposit1Rate
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        )
                        .divide(
                                BigDecimal.valueOf(12),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal reward =
                deposit1Balance
                        .multiply(monthlyRate)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        deposit1Balance =
                deposit1Balance.add(reward);

        deposit1Reward =
                deposit1Reward.add(reward);

        deposit1SimulatedMonths++;

        return reward;
    }

    public BigDecimal simulateSecondDepositMonth() {

        if (deposit2Rate == null) {
            return BigDecimal.ZERO;
        }

        if (deposit2SimulatedMonths >= deposit2TermMonths) {
            return BigDecimal.ZERO;
        }

        BigDecimal monthlyRate =
                deposit2Rate
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        )
                        .divide(
                                BigDecimal.valueOf(12),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal reward =
                deposit2Balance
                        .multiply(monthlyRate)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        deposit2Balance =
                deposit2Balance.add(reward);

        deposit2Reward =
                deposit2Reward.add(reward);

        deposit2SimulatedMonths++;

        return reward;
    }

    public BigDecimal closeFirstDepositEarly() {

        BigDecimal burnedReward =
                deposit1Reward;

        deposit1Balance =
                deposit1Balance.subtract(
                        deposit1Reward
                );

        deposit1Reward = BigDecimal.ZERO;

        return burnedReward;
    }

    public BigDecimal closeSecondDepositEarly() {

        BigDecimal burnedReward =
                deposit2Reward;

        deposit2Balance =
                deposit2Balance.subtract(
                        deposit2Reward
                );

        deposit2Reward = BigDecimal.ZERO;

        return burnedReward;
    }

    public void depositToFirst(BigDecimal amount) {
        deposit1Balance =
                deposit1Balance.add(amount);
    }

    public void withdrawFromFirst(BigDecimal amount) {

        if (deposit1Balance.compareTo(amount) >= 0) {

            deposit1Balance =
                    deposit1Balance.subtract(amount);

        } else {

            System.out.println(
                    "Недостаточно денег на первом депозите!"
            );
        }
    }

    public void depositToSecond(BigDecimal amount) {
        deposit2Balance =
                deposit2Balance.add(amount);
    }

    public void transferFirstToSecond(
            BigDecimal amount
    ) {

        if (deposit1Balance.compareTo(amount) >= 0) {

            deposit1Balance =
                    deposit1Balance.subtract(amount);

            deposit2Balance =
                    deposit2Balance.add(amount);

        } else {

            System.out.println(
                    "Недостаточно денег на первом депозите!"
            );
        }
    }

    public void transferSecondToFirst(
            BigDecimal amount
    ) {

        if (deposit2Balance.compareTo(amount) >= 0) {

            deposit2Balance =
                    deposit2Balance.subtract(amount);

            deposit1Balance =
                    deposit1Balance.add(amount);

        } else {

            System.out.println(
                    "Недостаточно денег на втором депозите!"
            );
        }
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getDeposit1Number() {
        return deposit1Number;
    }

    public String getDeposit2Number() {
        return deposit2Number;
    }

    public BigDecimal getDeposit1Balance() {
        return deposit1Balance;
    }

    public BigDecimal getDeposit2Balance() {
        return deposit2Balance;
    }

    public BigDecimal getDeposit1Rate() {
        return deposit1Rate;
    }

    public int getDeposit1TermMonths() {
        return deposit1TermMonths;
    }

    public LocalDate getDeposit1OpenDate() {
        return deposit1OpenDate;
    }

    public LocalDate getDeposit1EndDate() {
        return deposit1EndDate;
    }

    public int getDeposit1SimulatedMonths() {
        return deposit1SimulatedMonths;
    }

    public BigDecimal getDeposit1Reward() {
        return deposit1Reward;
    }

    public BigDecimal getDeposit2Rate() {
        return deposit2Rate;
    }

    public int getDeposit2TermMonths() {
        return deposit2TermMonths;
    }

    public LocalDate getDeposit2OpenDate() {
        return deposit2OpenDate;
    }

    public LocalDate getDeposit2EndDate() {
        return deposit2EndDate;
    }

    public int getDeposit2SimulatedMonths() {
        return deposit2SimulatedMonths;
    }

    public BigDecimal getDeposit2Reward() {
        return deposit2Reward;
    }
}
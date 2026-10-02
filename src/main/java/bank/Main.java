package bank;

import bank.account.Account;
import bank.account.AccountRepository;
import bank.customer.Customer;
import bank.customer.CustomerRepository;
import bank.database.Database;
import bank.transaction.Transaction;
import bank.transaction.TransactionRepository;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Database.createTables();

        Scanner scanner = new Scanner(System.in);

        CustomerRepository customerRepository =
                new CustomerRepository();

        AccountRepository accountRepository =
                new AccountRepository();

        TransactionRepository transactionRepository =
                new TransactionRepository();

        int choice;

        do {

            System.out.println();
            System.out.println("===== БАНКОВСКАЯ СИСТЕМА =====");
            System.out.println("1. Создать клиента");
            System.out.println("2. Пополнить депозит");
            System.out.println("3. Снять деньги");
            System.out.println("4. Перевести между депозитами");
            System.out.println("5. Показать баланс");
            System.out.println("6. История транзакций");
            System.out.println("7. Добавить месяц");
            System.out.println("8. Досрочно закрыть депозит");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            choice = scanner.nextInt();

            switch (choice) {

                case 1:

                    scanner.nextLine();

                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();

                    System.out.print("Введите фамилию: ");
                    String surname = scanner.nextLine();

                    System.out.print("Введите email: ");
                    String email = scanner.nextLine();

                    Customer customer =
                            new Customer(
                                    name,
                                    surname,
                                    email
                            );

                    int customerId =
                            customerRepository.save(customer);

                    if (customerId == -1) {
                        System.out.println(
                                "Не удалось создать клиента."
                        );
                        break;
                    }

                    System.out.print(
                            "Введите номер первого депозита: "
                    );
                    String deposit1Number =
                            scanner.nextLine();

                    System.out.print(
                            "Введите номер второго депозита: "
                    );
                    String deposit2Number =
                            scanner.nextLine();

                    Account account =
                            new Account(
                                    customerId,
                                    deposit1Number,
                                    deposit2Number
                            );

                    System.out.print(
                            "Введите ставку первого депозита: "
                    );
                    BigDecimal rate1 =
                            scanner.nextBigDecimal();

                    System.out.print(
                            "Введите срок первого депозита " +
                                    "(3, 6, 9 или 12): "
                    );
                    int term1 = scanner.nextInt();

                    boolean firstOpened =
                            account.openFirstDeposit(
                                    rate1,
                                    term1
                            );

                    if (!firstOpened) {
                        System.out.println(
                                "Неверный срок первого депозита."
                        );
                        break;
                    }

                    System.out.print(
                            "Введите ставку второго депозита: "
                    );
                    BigDecimal rate2 =
                            scanner.nextBigDecimal();

                    System.out.print(
                            "Введите срок второго депозита " +
                                    "(3, 6, 9 или 12): "
                    );
                    int term2 = scanner.nextInt();

                    boolean secondOpened =
                            account.openSecondDeposit(
                                    rate2,
                                    term2
                            );

                    if (!secondOpened) {
                        System.out.println(
                                "Неверный срок второго депозита."
                        );
                        break;
                    }

                    int accountId =
                            accountRepository.save(account);

                    if (accountId == -1) {
                        System.out.println(
                                "Не удалось создать аккаунт."
                        );
                        break;
                    }

                    System.out.println(
                            "Клиент и аккаунт успешно созданы."
                    );

                    System.out.println(
                            "Дата окончания первого депозита: "
                                    + account.getDeposit1EndDate()
                    );

                    System.out.println(
                            "Дата окончания второго депозита: "
                                    + account.getDeposit2EndDate()
                    );

                    break;

                case 2:

                    System.out.print("Введите ID аккаунта: ");
                    int id = scanner.nextInt();

                    Account foundAccount =
                            accountRepository.findById(id);

                    if (foundAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.print(
                            "Какой депозит пополнить (1 или 2): "
                    );
                    int depositNumber =
                            scanner.nextInt();

                    System.out.print("Введите сумму: ");
                    BigDecimal amount =
                            scanner.nextBigDecimal();

                    if (amount.compareTo(
                            BigDecimal.ZERO) <= 0) {

                        System.out.println(
                                "Сумма должна быть больше нуля."
                        );
                        break;
                    }

                    if (depositNumber == 1) {

                        foundAccount.depositToFirst(amount);

                    } else if (depositNumber == 2) {

                        foundAccount.depositToSecond(amount);

                    } else {

                        System.out.println(
                                "Такого депозита нет."
                        );
                        break;
                    }

                    accountRepository.update(
                            foundAccount
                    );

                    Transaction depositTransaction =
                            new Transaction(
                                    foundAccount.getId(),
                                    amount,
                                    "DEPOSIT_" + depositNumber
                            );

                    transactionRepository.save(
                            depositTransaction
                    );

                    System.out.println(
                            "Депозит успешно пополнен."
                    );

                    break;

                case 3:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int withdrawAccountId =
                            scanner.nextInt();

                    Account withdrawAccount =
                            accountRepository.findById(
                                    withdrawAccountId
                            );

                    if (withdrawAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.println(
                            "Снимать деньги можно только " +
                                    "с первого депозита."
                    );

                    System.out.print(
                            "Введите сумму для снятия: "
                    );
                    BigDecimal withdrawAmount =
                            scanner.nextBigDecimal();

                    if (withdrawAmount.compareTo(
                            BigDecimal.ZERO) <= 0) {

                        System.out.println(
                                "Сумма должна быть больше нуля."
                        );
                        break;
                    }

                    if (withdrawAccount
                            .getDeposit1Balance()
                            .compareTo(withdrawAmount) < 0) {

                        System.out.println(
                                "Недостаточно денег."
                        );
                        break;
                    }

                    withdrawAccount.withdrawFromFirst(
                            withdrawAmount
                    );

                    accountRepository.update(
                            withdrawAccount
                    );

                    Transaction withdrawTransaction =
                            new Transaction(
                                    withdrawAccount.getId(),
                                    withdrawAmount,
                                    "WITHDRAW"
                            );

                    transactionRepository.save(
                            withdrawTransaction
                    );

                    System.out.println(
                            "Деньги успешно сняты."
                    );

                    break;

                case 4:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int transferAccountId =
                            scanner.nextInt();

                    Account transferAccount =
                            accountRepository.findById(
                                    transferAccountId
                            );

                    if (transferAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.println(
                            "1. С первого депозита на второй"
                    );
                    System.out.println(
                            "2. Со второго депозита на первый"
                    );
                    System.out.print(
                            "Выберите направление: "
                    );

                    int direction =
                            scanner.nextInt();

                    System.out.print(
                            "Введите сумму перевода: "
                    );
                    BigDecimal transferAmount =
                            scanner.nextBigDecimal();

                    if (transferAmount.compareTo(
                            BigDecimal.ZERO) <= 0) {

                        System.out.println(
                                "Сумма должна быть больше нуля."
                        );
                        break;
                    }

                    if (direction == 1) {

                        if (transferAccount
                                .getDeposit1Balance()
                                .compareTo(
                                        transferAmount
                                ) < 0) {

                            System.out.println(
                                    "Недостаточно денег."
                            );
                            break;
                        }

                        transferAccount
                                .transferFirstToSecond(
                                        transferAmount
                                );

                    } else if (direction == 2) {

                        if (transferAccount
                                .getDeposit2Balance()
                                .compareTo(
                                        transferAmount
                                ) < 0) {

                            System.out.println(
                                    "Недостаточно денег."
                            );
                            break;
                        }

                        transferAccount
                                .transferSecondToFirst(
                                        transferAmount
                                );

                    } else {

                        System.out.println(
                                "Неверное направление."
                        );
                        break;
                    }

                    accountRepository.update(
                            transferAccount
                    );

                    Transaction transferTransaction =
                            new Transaction(
                                    transferAccount.getId(),
                                    transferAmount,
                                    "TRANSFER"
                            );

                    transactionRepository.save(
                            transferTransaction
                    );

                    System.out.println(
                            "Перевод выполнен."
                    );

                    break;

                case 5:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int balanceAccountId =
                            scanner.nextInt();

                    Account balanceAccount =
                            accountRepository.findById(
                                    balanceAccountId
                            );

                    if (balanceAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.println(
                            "===== ПЕРВЫЙ ДЕПОЗИТ ====="
                    );
                    System.out.println(
                            "Номер: "
                                    + balanceAccount
                                    .getDeposit1Number()
                    );
                    System.out.println(
                            "Баланс: "
                                    + balanceAccount
                                    .getDeposit1Balance()
                    );
                    System.out.println(
                            "Ставка: "
                                    + balanceAccount
                                    .getDeposit1Rate()
                                    + "%"
                    );
                    System.out.println(
                            "Срок: "
                                    + balanceAccount
                                    .getDeposit1TermMonths()
                                    + " мес."
                    );
                    System.out.println(
                            "Дата открытия: "
                                    + balanceAccount
                                    .getDeposit1OpenDate()
                    );
                    System.out.println(
                            "Дата окончания: "
                                    + balanceAccount
                                    .getDeposit1EndDate()
                    );
                    System.out.println(
                            "Пройдено месяцев: "
                                    + balanceAccount
                                    .getDeposit1SimulatedMonths()
                    );
                    System.out.println(
                            "Вознаграждение: "
                                    + balanceAccount
                                    .getDeposit1Reward()
                    );

                    System.out.println(
                            "===== ВТОРОЙ ДЕПОЗИТ ====="
                    );
                    System.out.println(
                            "Номер: "
                                    + balanceAccount
                                    .getDeposit2Number()
                    );
                    System.out.println(
                            "Баланс: "
                                    + balanceAccount
                                    .getDeposit2Balance()
                    );
                    System.out.println(
                            "Ставка: "
                                    + balanceAccount
                                    .getDeposit2Rate()
                                    + "%"
                    );
                    System.out.println(
                            "Срок: "
                                    + balanceAccount
                                    .getDeposit2TermMonths()
                                    + " мес."
                    );
                    System.out.println(
                            "Дата открытия: "
                                    + balanceAccount
                                    .getDeposit2OpenDate()
                    );
                    System.out.println(
                            "Дата окончания: "
                                    + balanceAccount
                                    .getDeposit2EndDate()
                    );
                    System.out.println(
                            "Пройдено месяцев: "
                                    + balanceAccount
                                    .getDeposit2SimulatedMonths()
                    );
                    System.out.println(
                            "Вознаграждение: "
                                    + balanceAccount
                                    .getDeposit2Reward()
                    );

                    break;

                case 6:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int historyAccountId =
                            scanner.nextInt();

                    System.out.println(
                            "===== ИСТОРИЯ ТРАНЗАКЦИЙ ====="
                    );

                    transactionRepository.showByAccountId(
                            historyAccountId
                    );

                    break;

                case 7:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int simulateAccountId =
                            scanner.nextInt();

                    Account simulateAccount =
                            accountRepository.findById(
                                    simulateAccountId
                            );

                    if (simulateAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.print(
                            "Какой депозит симулировать (1 или 2): "
                    );
                    int simulateDeposit =
                            scanner.nextInt();

                    BigDecimal reward;

                    if (simulateDeposit == 1) {

                        if (simulateAccount
                                .getDeposit1SimulatedMonths()
                                >= simulateAccount
                                .getDeposit1TermMonths()) {

                            System.out.println(
                                    "Срок депозита уже закончился."
                            );
                            break;
                        }

                        reward =
                                simulateAccount
                                        .simulateFirstDepositMonth();

                    } else if (simulateDeposit == 2) {

                        if (simulateAccount
                                .getDeposit2SimulatedMonths()
                                >= simulateAccount
                                .getDeposit2TermMonths()) {

                            System.out.println(
                                    "Срок депозита уже закончился."
                            );
                            break;
                        }

                        reward =
                                simulateAccount
                                        .simulateSecondDepositMonth();

                    } else {

                        System.out.println(
                                "Такого депозита нет."
                        );
                        break;
                    }

                    accountRepository.update(
                            simulateAccount
                    );

                    Transaction rewardTransaction =
                            new Transaction(
                                    simulateAccount.getId(),
                                    reward,
                                    "REWARD_" + simulateDeposit
                            );

                    transactionRepository.save(
                            rewardTransaction
                    );

                    System.out.println(
                            "Прошел 1 месяц."
                    );
                    System.out.println(
                            "Начислено вознаграждение: "
                                    + reward
                    );

                    break;

                case 8:

                    System.out.print(
                            "Введите ID аккаунта: "
                    );
                    int closeAccountId =
                            scanner.nextInt();

                    Account closeAccount =
                            accountRepository.findById(
                                    closeAccountId
                            );

                    if (closeAccount == null) {
                        System.out.println(
                                "Аккаунт не найден."
                        );
                        break;
                    }

                    System.out.print(
                            "Какой депозит закрыть досрочно " +
                                    "(1 или 2): "
                    );
                    int closeDeposit =
                            scanner.nextInt();

                    BigDecimal burnedReward;

                    if (closeDeposit == 1) {

                        burnedReward =
                                closeAccount
                                        .closeFirstDepositEarly();

                    } else if (closeDeposit == 2) {

                        burnedReward =
                                closeAccount
                                        .closeSecondDepositEarly();

                    } else {

                        System.out.println(
                                "Такого депозита нет."
                        );
                        break;
                    }

                    accountRepository.update(
                            closeAccount
                    );

                    Transaction cancelTransaction =
                            new Transaction(
                                    closeAccount.getId(),
                                    burnedReward,
                                    "REWARD_CANCEL_"
                                            + closeDeposit
                            );

                    transactionRepository.save(
                            cancelTransaction
                    );

                    System.out.println(
                            "Депозит закрыт досрочно."
                    );
                    System.out.println(
                            "Сгорело вознаграждения: "
                                    + burnedReward
                    );

                    break;

                case 0:

                    System.out.println(
                            "Программа завершена."
                    );
                    break;

                default:

                    System.out.println(
                            "Такого пункта нет."
                    );
            }

        } while (choice != 0);

        scanner.close();
    }
}
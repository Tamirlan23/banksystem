package bank.account;

import bank.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AccountRepository {

    public int save(Account account) {

        String sql = """
                INSERT INTO accounts (
                    customer_id,
                    deposit1_number,
                    deposit2_number,
                    deposit1_balance,
                    deposit2_balance,
                    deposit1_rate,
                    deposit1_term_months,
                    deposit1_open_date,
                    deposit1_end_date,
                    deposit1_simulated_months,
                    deposit1_reward,
                    deposit2_rate,
                    deposit2_term_months,
                    deposit2_open_date,
                    deposit2_end_date,
                    deposit2_simulated_months,
                    deposit2_reward
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    account.getCustomerId()
            );

            statement.setString(
                    2,
                    account.getDeposit1Number()
            );

            statement.setString(
                    3,
                    account.getDeposit2Number()
            );

            statement.setBigDecimal(
                    4,
                    account.getDeposit1Balance()
            );

            statement.setBigDecimal(
                    5,
                    account.getDeposit2Balance()
            );

            statement.setBigDecimal(
                    6,
                    account.getDeposit1Rate()
            );

            statement.setInt(
                    7,
                    account.getDeposit1TermMonths()
            );

            statement.setObject(
                    8,
                    account.getDeposit1OpenDate()
            );

            statement.setObject(
                    9,
                    account.getDeposit1EndDate()
            );

            statement.setInt(
                    10,
                    account.getDeposit1SimulatedMonths()
            );

            statement.setBigDecimal(
                    11,
                    account.getDeposit1Reward()
            );

            statement.setBigDecimal(
                    12,
                    account.getDeposit2Rate()
            );

            statement.setInt(
                    13,
                    account.getDeposit2TermMonths()
            );

            statement.setObject(
                    14,
                    account.getDeposit2OpenDate()
            );

            statement.setObject(
                    15,
                    account.getDeposit2EndDate()
            );

            statement.setInt(
                    16,
                    account.getDeposit2SimulatedMonths()
            );

            statement.setBigDecimal(
                    17,
                    account.getDeposit2Reward()
            );

            statement.executeUpdate();

            ResultSet result =
                    statement.getGeneratedKeys();

            if (result.next()) {

                int accountId =
                        result.getInt(1);

                System.out.println(
                        "Аккаунт создан. ID аккаунта: "
                                + accountId
                );

                return accountId;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка сохранения аккаунта: "
                            + e.getMessage()
            );
        }

        return -1;
    }

    public Account findById(int id) {

        String sql = """
                SELECT *
                FROM accounts
                WHERE id = ?
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet result =
                    statement.executeQuery();

            if (result.next()) {

                return new Account(
                        result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getString("deposit1_number"),
                        result.getString("deposit2_number"),
                        result.getBigDecimal("deposit1_balance"),
                        result.getBigDecimal("deposit2_balance"),
                        result.getBigDecimal("deposit1_rate"),
                        result.getInt("deposit1_term_months"),
                        result.getObject(
                                "deposit1_open_date",
                                LocalDate.class
                        ),
                        result.getObject(
                                "deposit1_end_date",
                                LocalDate.class
                        ),
                        result.getInt(
                                "deposit1_simulated_months"
                        ),
                        result.getBigDecimal(
                                "deposit1_reward"
                        ),
                        result.getBigDecimal("deposit2_rate"),
                        result.getInt("deposit2_term_months"),
                        result.getObject(
                                "deposit2_open_date",
                                LocalDate.class
                        ),
                        result.getObject(
                                "deposit2_end_date",
                                LocalDate.class
                        ),
                        result.getInt(
                                "deposit2_simulated_months"
                        ),
                        result.getBigDecimal(
                                "deposit2_reward"
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка поиска аккаунта: "
                            + e.getMessage()
            );
        }

        return null;
    }

    public void update(Account account) {

        String sql = """
                UPDATE accounts
                SET deposit1_balance = ?,
                    deposit2_balance = ?,
                    deposit1_rate = ?,
                    deposit1_term_months = ?,
                    deposit1_open_date = ?,
                    deposit1_end_date = ?,
                    deposit1_simulated_months = ?,
                    deposit1_reward = ?,
                    deposit2_rate = ?,
                    deposit2_term_months = ?,
                    deposit2_open_date = ?,
                    deposit2_end_date = ?,
                    deposit2_simulated_months = ?,
                    deposit2_reward = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setBigDecimal(
                    1,
                    account.getDeposit1Balance()
            );

            statement.setBigDecimal(
                    2,
                    account.getDeposit2Balance()
            );

            statement.setBigDecimal(
                    3,
                    account.getDeposit1Rate()
            );

            statement.setInt(
                    4,
                    account.getDeposit1TermMonths()
            );

            statement.setObject(
                    5,
                    account.getDeposit1OpenDate()
            );

            statement.setObject(
                    6,
                    account.getDeposit1EndDate()
            );

            statement.setInt(
                    7,
                    account.getDeposit1SimulatedMonths()
            );

            statement.setBigDecimal(
                    8,
                    account.getDeposit1Reward()
            );

            statement.setBigDecimal(
                    9,
                    account.getDeposit2Rate()
            );

            statement.setInt(
                    10,
                    account.getDeposit2TermMonths()
            );

            statement.setObject(
                    11,
                    account.getDeposit2OpenDate()
            );

            statement.setObject(
                    12,
                    account.getDeposit2EndDate()
            );

            statement.setInt(
                    13,
                    account.getDeposit2SimulatedMonths()
            );

            statement.setBigDecimal(
                    14,
                    account.getDeposit2Reward()
            );

            statement.setInt(
                    15,
                    account.getId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Данные аккаунта обновлены."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка обновления аккаунта: "
                            + e.getMessage()
            );
        }
    }
}
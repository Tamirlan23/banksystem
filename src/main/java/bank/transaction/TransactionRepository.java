package bank.transaction;

import bank.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionRepository {


    public void save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (account_id, amount, type, transaction_time)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    transaction.getAccountId()
            );

            statement.setBigDecimal(
                    2,
                    transaction.getAmount()
            );

            statement.setString(
                    3,
                    transaction.getType()
            );

            statement.setTimestamp(
                    4,
                    Timestamp.valueOf(transaction.getTime())
            );

            statement.executeUpdate();

            System.out.println("Транзакция сохранена.");

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка сохранения транзакции: "
                            + e.getMessage()
            );
        }
    }



    public void showByAccountId(int accountId) {

        String sql = """
                SELECT *
                FROM transactions
                WHERE account_id = ?
                ORDER BY id
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, accountId);

            ResultSet result =
                    statement.executeQuery();

            while (result.next()) {

                System.out.println(
                        "ID: " + result.getInt("id")
                                + " | Сумма: "
                                + result.getBigDecimal("amount")
                                + " | Тип: "
                                + result.getString("type")
                                + " | Время: "
                                + result.getTimestamp(
                                "transaction_time"
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка получения транзакций: "
                            + e.getMessage()
            );
        }
    }
}
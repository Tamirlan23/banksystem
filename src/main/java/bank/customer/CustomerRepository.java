package bank.customer;

import bank.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepository {

    public int save(Customer customer) {

        String sql = """
                INSERT INTO customers (name, surname, email)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    customer.getName()
            );

            statement.setString(
                    2,
                    customer.getSurname()
            );

            statement.setString(
                    3,
                    customer.getEmail()
            );

            statement.executeUpdate();

            ResultSet result =
                    statement.getGeneratedKeys();

            if (result.next()) {

                int customerId =
                        result.getInt(1);

                System.out.println(
                        "Клиент создан. ID клиента: "
                                + customerId
                );

                return customerId;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Ошибка сохранения клиента: "
                            + e.getMessage()
            );
        }

        return -1;
    }
}
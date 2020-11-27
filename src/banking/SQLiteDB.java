package banking;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class SQLiteDB implements DataBase {
    String url;

    private static final String SQL_ADD_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SQL_DELETE_ACCOUNT = "DELETE FROM card WHERE number = ?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE card SET balance = balance + ? WHERE number = ?";
    private static final String SQL_FIND_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ?";


    public SQLiteDB(String dbName) {
        url = "jdbc:sqlite:./" + dbName;
        File file = new File("./" + dbName);
        if (!file.exists()) {
            createNewDatabase();
        }
        createNewTableIfNotExist();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createNewTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Account createAccount(String cardNumber, String pin) {
        Account account = new Account(cardNumber, pin);
        try (Connection connection = connect();
             final var sql = connection.prepareStatement(SQL_ADD_ACCOUNT)) {
            System.out.println("Creating account");
            sql.setString(1, account.getCardNumber());
            sql.setString(2, account.getPin());
            sql.executeUpdate();
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM card";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(rs.getString("number"), rs.getString("pin"), rs.getLong("balance")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    @Override
    public Account findAccount(String creditCardNumber) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_FIND_ACCOUNT)) {
            sql.setString(1, creditCardNumber);
            final var resultSet = sql.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new Account(resultSet.getString("number"),
                    resultSet.getString("pin"),
                    resultSet.getInt("balance"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Account updateAccount(String cardNumber, long value) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_UPDATE_ACCOUNT)) {

            sql.setLong(1, value);
            sql.setString(2, cardNumber);
            sql.executeUpdate();

            return findAccount(cardNumber);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteAccount(Account account) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_DELETE_ACCOUNT)) {

            sql.setString(1, account.getCardNumber());
            sql.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

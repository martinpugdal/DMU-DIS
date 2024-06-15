import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        String user = "sa";
        String password = "Kode2023";
        String database = "bank";
        String url = "jdbc:sqlserver://localhost;" + "databaseName=" + database + ";";
        url += "user=" + user + ";" + "password=" + password;

        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (con == null) {
            System.out.println("Connection failed");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ktoNr: ");
        String fromKtoNr = scanner.next();

        System.out.print("Send money to: ");
        String toKtoNr = scanner.next();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        con.setAutoCommit(false);
        //con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM konto WHERE ktoNr = " + fromKtoNr);
        if (!resultSet.next()) {
            System.out.println("Account not found");
            con.rollback();
            return;
        }
        double balance = resultSet.getDouble("saldo");

        if (balance < amount) {
            System.out.println("Insufficient funds");
            con.rollback();
            return;
        }

        resultSet = con.createStatement().executeQuery("SELECT * FROM konto WHERE ktoNr = " + toKtoNr);
        if (!resultSet.next()) {
            System.out.println("Account not found");
            con.rollback();
            return;
        }
        double toBalance = resultSet.getDouble("saldo");

        // a way to keepgate the transaction before it is done
        System.out.print("Confirm transaction? (y/n): ");
        String confirm = scanner.next();
        if (!confirm.equals("y")) {
            con.rollback();
            return;
        }

        double newBalance = balance - amount;
        double newToBalance = toBalance + amount;
        con.createStatement().executeUpdate("UPDATE konto SET saldo = " + newBalance + " WHERE ktoNr = " + fromKtoNr);
        con.createStatement().executeUpdate("UPDATE konto SET saldo = " + newToBalance + " WHERE ktoNr = " + toKtoNr);

        con.commit();
        System.out.println("Transaction successful");
    }
}
package PasswordOpgave;

import javafx.application.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

	public static Connection con;
	
	public static void main(String[] args) {
		String user = "sa";
		String password = "Kode2023";
		String database = "password";
		String url = "jdbc:sqlserver://localhost;" + "databaseName=" + database + ";encrypt=true;trustServerCertificate=true;";
		url += "user=" + user + ";" + "password=" + password;

		try {
			con = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (con == null) {
			System.out.println("Connection failed");
			return;
		}

		Application.launch(Gui.class);
	}
}

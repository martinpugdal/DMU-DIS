package PasswordOpgave;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gui extends Application {
    private static TextField brugernavn = new TextField();
    private final Connection con = App.con;
    Button btnLogin, btnOpret, btnscene2;
    Label lblbrugernavn, lblPassword, lblBesked;
    Label lblscene2, lblInfoBruger;
    GridPane pane1, pane2;
    Scene scene1, scene2;
    Stage thestage;
    private PasswordField password = new PasswordField();

    @Override
    public void start(Stage primaryStage) {

        thestage = primaryStage;

        btnLogin = new Button("Log in");
        btnOpret = new Button("Opret");
        btnscene2 = new Button("Tilbage til log in");
        btnLogin.setOnAction(e -> {
            try {
                ButtonClicked(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnOpret.setOnAction(e -> {
            try {
                ButtonClicked(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnscene2.setOnAction(e -> {
            try {
                ButtonClicked(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        lblbrugernavn = new Label("Navn");
        lblPassword = new Label("Password");
        lblBesked = new Label("");

        lblscene2 = new Label("Du er nu logget ind");
        lblInfoBruger = new Label("Bruger info");

        pane1 = new GridPane();
        pane2 = new GridPane();
        pane1.setVgap(10);
        pane2.setVgap(10);

        pane1.setStyle("-fx-background-color: yellow;-fx-padding: 10px;");
        pane2.setStyle("-fx-background-color: lightgreen;-fx-padding: 10px;");

        pane1.setPadding(new Insets(5));
        pane1.setHgap(10);
        pane1.setVgap(10);

        pane1.add(lblbrugernavn, 0, 0);
        pane1.add(brugernavn, 0, 1, 2, 1);
        pane1.add(lblPassword, 0, 2);
        pane1.add(password, 0, 3, 2, 1);
        pane1.add(btnLogin, 0, 4);
        pane1.add(btnOpret, 1, 4);
        pane1.add(lblBesked, 0, 5);

        pane2.setPadding(new Insets(5));
        pane2.setHgap(10);
        pane2.setVgap(10);

        pane2.add(lblscene2, 0, 0);
        pane2.add(btnscene2, 0, 1);

        scene1 = new Scene(pane1, 200, 200);
        scene2 = new Scene(pane2, 200, 200);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public void ButtonClicked(ActionEvent e) throws SQLException {
        if (e.getSource() == btnLogin) {
            String bruger = brugernavn.getText();
            String pass = password.getText();
            if (authenticateUser(bruger, pass)) {
                lblscene2.setText(lblscene2.getText() + " " + bruger);
                thestage.setScene(scene2);
                lblBesked.setText("Login successful");
            } else {
                lblBesked.setText("Invalid credentials");
            }
        } else if (e.getSource() == btnOpret) {
            String bruger = brugernavn.getText();
            String pass = password.getText();
            if (userExists(bruger)) {
                lblBesked.setText("Username already exists");
                return;
            }
            if (createUser(bruger, pass)) {
                lblBesked.setText("User created successfully");
            } else {
                lblBesked.setText("User creation failed");
            }
            password.clear();
            brugernavn.clear();
            System.out.println("Oprettet");
        } else {
            lblBesked.setText("");
            password.clear();
            brugernavn.clear();
            thestage.setScene(scene1);
        }
    }

    private boolean createUser(String username, String password) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
        ps.setString(1, username);
        ps.setString(2, hashPassword(password));
        ps.executeUpdate();
        return true;
    }

    private boolean userExists(String username) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private boolean authenticateUser(String username, String password) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        ps.setString(1, username);
        ps.setString(2, hashPassword(password));
        return ps.executeQuery().next();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package org.example.integradora2ced.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private Label lblResultado;

    @FXML
    private Label lblMensaje;

    @FXML
    private Button btnReintentar;

    @FXML
    private Button btnMenu;

    private String nivelActual;
    private boolean victoria;

    public void setDatos(String nivel, boolean victoria) {
        this.nivelActual = nivel;
        this.victoria = victoria;

        if (victoria) {
            lblResultado.setText("¡VICTORIA!");
            lblResultado.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
            lblMensaje.setText("¡Llegaste a la meta!");
        } else {
            lblResultado.setText("GAME OVER");
            lblResultado.setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");
            lblMensaje.setText("Los enemigos te atraparon");
        }
    }

    @FXML
    private void reintentar() {
        try {
            String fxmlPath = nivelActual.equals("nivel1")
                    ? "/org/example/integradora2ced/level1-view.fxml"
                    : "/org/example/integradora2ced/level2-view.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) btnReintentar.getScene().getWindow();
            Scene scene = new Scene(root, 750, 750);
            stage.setScene(scene);
            stage.setTitle("Laberinto - " + (nivelActual.equals("nivel1") ? "Nivel 1" : "Nivel 2"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al reintentar: " + e.getMessage());
        }
    }

    @FXML
    private void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/integradora2ced/menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            Scene scene = new Scene(root, 750, 750);
            stage.setScene(scene);
            stage.setTitle("Laberinto - Menú Principal");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al volver al menú: " + e.getMessage());
        }
    }
}
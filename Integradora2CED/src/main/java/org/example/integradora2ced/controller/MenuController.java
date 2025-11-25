package org.example.integradora2ced.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button btnNivel1;

    @FXML
    private Button btnNivel2;

    @FXML
    private void handleNivel1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/integradora2ced/level1-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnNivel1.getScene().getWindow();
            Scene scene = new Scene(root, 750, 750);
            stage.setScene(scene);
            stage.setTitle("Laberinto - Nivel 1");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar Nivel 1: " + e.getMessage());
        }
    }

    @FXML
    private void handleNivel2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/integradora2ced/level2-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnNivel2.getScene().getWindow();
            Scene scene = new Scene(root, 750, 750);
            stage.setScene(scene);
            stage.setTitle("Laberinto - Nivel 2");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar Nivel 2: " + e.getMessage());
        }
    }
}
package org.example.integradora2ced.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.integradora2ced.model.Nivel2;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelTwoController implements Initializable {
    @FXML
    private Canvas canvas;

    @FXML
    private Button btnMenu;

    private GraphicsContext gc;
    private Nivel2 nivel2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.gc = canvas.getGraphicsContext2D();
        this.nivel2 = new Nivel2(this.canvas);

        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        canvas.setOnKeyPressed(this::handleKeyPress);

        // Hilo de renderizado
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    nivel2.paint();
                    verificarEstadoJuego();
                });

                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        boolean movimientoExitoso = false;

        switch (code) {
            case UP:
            case W:
                movimientoExitoso = nivel2.getJugador().mover("ARRIBA");
                break;
            case DOWN:
            case S:
                movimientoExitoso = nivel2.getJugador().mover("ABAJO");
                break;
            case LEFT:
            case A:
                movimientoExitoso = nivel2.getJugador().mover("IZQUIERDA");
                break;
            case RIGHT:
            case D:
                movimientoExitoso = nivel2.getJugador().mover("DERECHA");
                break;
        }

        if (movimientoExitoso) {
            System.out.println("Jugador en nodo: " + nivel2.getJugador().getNodoActual().getData());

            new Thread(() -> {
                while (nivel2.getJugador().estaMoviendose()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(() -> {
                    nivel2.getEnemigo1().calcularMovimiento(nivel2.getJugador().getNodoActual());
                    nivel2.getEnemigo2().calcularMovimiento(nivel2.getJugador().getNodoActual());
                });
            }).start();
        }

        event.consume();
    }

    /**
     * Verifica si el jugador ganó o perdió
     */
    private void verificarEstadoJuego() {
        if (nivel2.getJugador().getNodoActual() == nivel2.getNodoFinal()
                && !nivel2.getJugador().estaMoviendose()) {
            mostrarPantallaFinal(true);
        }

        if (!nivel2.getJugador().estaMoviendose() &&
                !nivel2.getEnemigo1().estaMoviendose() &&
                !nivel2.getEnemigo2().estaMoviendose()) {

            if (nivel2.getEnemigo1().getNodoActual() == nivel2.getJugador().getNodoActual() ||
                    nivel2.getEnemigo2().getNodoActual() == nivel2.getJugador().getNodoActual()) {
                mostrarPantallaFinal(false);
            }
        }
    }

    private boolean pantallaFinalMostrada = false;

    private void mostrarPantallaFinal(boolean victoria) {
        if (pantallaFinalMostrada) {
            return;
        }
        pantallaFinalMostrada = true;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/integradora2ced/game-over-view.fxml"));
            Parent root = loader.load();

            GameOverController controller = loader.getController();
            controller.setDatos("nivel2", victoria);

            Stage stage = (Stage) canvas.getScene().getWindow();
            Scene scene = new Scene(root, 750, 750);
            stage.setScene(scene);
            stage.setTitle("Laberinto - " + (victoria ? "Victoria" : "Game Over"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al mostrar pantalla final: " + e.getMessage());
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
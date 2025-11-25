package org.example.integradora2ced.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.integradora2ced.model.structures.Nodo;
import java.util.ArrayList;
import java.util.Objects;

public class Jugador {

    private Canvas canvas;
    private GraphicsContext gc;

    private ArrayList<Image> moviendo;
    private Image quieto;

    private Position position;
    private Position targetPosition;

    private Nodo<Integer> nodoActual;
    private Nodo<Integer> nodoObjetivo;

    private int state;
    private int frame;

    private static final double SPEED = 4.0;
    private static final double NODE_SIZE = 30;

    public Jugador(Canvas canvas, Nodo<Integer> nodo) {
        this.canvas = canvas;
        this.gc = this.canvas.getGraphicsContext2D();
        this.nodoActual = nodo;
        this.nodoObjetivo = null;
        this.frame = 0;
        this.state = 0;
        this.position = new Position(nodo.getPosition().getX(), nodo.getPosition().getY());
        this.targetPosition = null;
        this.moviendo = new ArrayList<>();

        Image image = new Image(getClass().getResourceAsStream("/org/example/integradora2ced/jugador/jugador (2).png"),15,15,false,false);
        this.quieto =  image;

        for (int i = 0; i < 4; i++) {
            Image imagen = new Image(getClass().getResourceAsStream("/org/example/integradora2ced/jugador/jugador ("+(i+1)+").png"), 15, 15, false, false);
            moviendo.add(imagen);
        }
    }
    /**
     * Metodo que pinta al jugador en el canvas
     */
    public void paint() {
        onMove();

        // Dibujar al jugador centrado en su posición
        double drawX = position.getX() + (NODE_SIZE - 15) / 2;
        double drawY = position.getY() + (NODE_SIZE - 15) / 2;

        if (state == 0) {
            gc.drawImage(quieto, drawX, drawY);
        } else if (state == 1) {
            gc.drawImage(moviendo.get(frame % 4), drawX, drawY);
            frame++;
        }
    }
    /**
     * Actualiza la informacion del jugador cuando se esta moviendo
     */
    public void onMove() {
        // Si hay un nodo objetivo, mover hacia él
        if (nodoObjetivo != null && targetPosition != null) {
            double dx = targetPosition.getX() - position.getX();
            double dy = targetPosition.getY() - position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < SPEED) {
                // Llegamos al destino
                position.setX(targetPosition.getX());
                position.setY(targetPosition.getY());
                nodoActual = nodoObjetivo;
                nodoObjetivo = null;
                targetPosition = null;
                state = 0; // Dejar de moverse
            } else {
                // Seguir moviéndose
                double moveX = (dx / distance) * SPEED;
                double moveY = (dy / distance) * SPEED;
                position.setX(position.getX() + moveX);
                position.setY(position.getY() + moveY);
                state = 1; // Moviéndose
            }
        }
    }

    /**
     * Intenta mover al jugador en una direccion
     * @param direccion direccion a la que quiere mover
     * @return true si el movimiento fue exitoso, false si no hay conexión
     */
    public boolean mover(String direccion) {
        if (nodoObjetivo != null) {
            return false;
        }

        Nodo<Integer> siguienteNodo = null;

        switch (direccion.toUpperCase()) {
            case "ARRIBA":
                siguienteNodo = nodoActual.getUpN();
                break;
            case "ABAJO":
                siguienteNodo = nodoActual.getDownN();
                break;
            case "IZQUIERDA":
                siguienteNodo = nodoActual.getLeftN();
                break;
            case "DERECHA":
                siguienteNodo = nodoActual.getRightN();
                break;
        }

        // Si hay un nodo conectado en esa dirección
        if (siguienteNodo != null) {
            nodoObjetivo = siguienteNodo;
            targetPosition = new Position(siguienteNodo.getPosition().getX(), siguienteNodo.getPosition().getY());
            state = 1; // Empezar a moverse
            return true;
        }

        return false;
    }

    /**
     * Hace que mientras se mueva, no pertenezca a ningun nodo y evitar errores
     * @return validacion
     */
    public boolean estaMoviendose() {
        return nodoObjetivo != null;
    }

    public Nodo<Integer> getNodoActual() {
        return nodoActual;
    }

    public Position getPosition() {
        return position;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public ArrayList<Image> getMoviendo() {
        return moviendo;
    }

    public void setMoviendo(ArrayList<Image> moviendo) {
        this.moviendo = moviendo;
    }

    public Image getQuieto() {
        return quieto;
    }

    public void setQuieto(Image quieto) {
        this.quieto = quieto;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public void setNodoActual(Nodo<Integer> nodoActual) {
        this.nodoActual = nodoActual;
    }

    public Nodo<Integer> getNodoObjetivo() {
        return nodoObjetivo;
    }

    public void setNodoObjetivo(Nodo<Integer> nodoObjetivo) {
        this.nodoObjetivo = nodoObjetivo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
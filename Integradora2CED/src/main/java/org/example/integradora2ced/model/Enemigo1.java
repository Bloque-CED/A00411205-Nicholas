package org.example.integradora2ced.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.integradora2ced.model.structures.Grafo;
import org.example.integradora2ced.model.structures.Nodo;

import java.util.*;

public class Enemigo1 {

    private Canvas canvas;
    private GraphicsContext gc;
    private Grafo<Integer> grafo;

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

    public Enemigo1(Canvas canvas, Nodo<Integer> nodo, Grafo<Integer> grafo) {
        this.canvas = canvas;
        this.gc = this.canvas.getGraphicsContext2D();
        this.grafo = grafo;
        this.nodoActual = nodo;
        this.nodoObjetivo = null;
        this.frame = 0;
        this.state = 0;
        this.position = new Position(nodo.getPosition().getX(), nodo.getPosition().getY());
        this.targetPosition = null;
        this.moviendo = new ArrayList<>();
        this.quieto = new Image(getClass().getResourceAsStream("/org/example/integradora2ced/enemigo/verde (2).png"), 15, 15, false, false);

        for (int i = 0; i < 4; i++) {
            moviendo.add(new Image(getClass().getResourceAsStream("/org/example/integradora2ced/enemigo/verde (" + (i + 1) + ").png"), 15, 15, false, false));
        }
    }

    /**
     * Metodo que pinta al enemigo en el canvas
     */
    public void paint() {
        onMove();

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
     * Actualiza la informacion del enemigo cuando se esta moviendo
     */
    public void onMove() {
        if (nodoObjetivo != null && targetPosition != null) {
            double dx = targetPosition.getX() - position.getX();
            double dy = targetPosition.getY() - position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < SPEED) {
                position.setX(targetPosition.getX());
                position.setY(targetPosition.getY());
                nodoActual = nodoObjetivo;
                nodoObjetivo = null;
                targetPosition = null;
                state = 0;
            } else {
                double moveX = (dx / distance) * SPEED;
                double moveY = (dy / distance) * SPEED;
                position.setX(position.getX() + moveX);
                position.setY(position.getY() + moveY);
                state = 1;
            }
        }
    }

    /**
     * Calcula el siguiente movimiento usando BFS hacia el jugador
     * @param nodoJugador nodo donde esta el jugador
     */
    public void calcularMovimiento(Nodo<Integer> nodoJugador) {
        if (nodoObjetivo != null) {
            return; // Ya está moviéndose
        }

        List<Nodo<Integer>> camino = bfs(nodoActual, nodoJugador);

        if (camino != null && camino.size() > 1) {
            // El siguiente nodo es el segundo en el camino (el primero es el actual)
            Nodo<Integer> siguienteNodo = camino.get(1);
            moverA(siguienteNodo);
        }
    }

    /**
     * Implementacion del metodo BFS en el grafo
     * @param inicio nodo inicial
     * @param destino nodo destino
     * @return Lista de todos los nodos del recorrido
     */
    private List<Nodo<Integer>> bfs(Nodo<Integer> inicio, Nodo<Integer> destino) {
        Queue<Nodo<Integer>> cola = new LinkedList<>();
        Map<Nodo<Integer>, Nodo<Integer>> padres = new HashMap<>();
        Set<Nodo<Integer>> visitados = new HashSet<>();

        cola.add(inicio);
        visitados.add(inicio);
        padres.put(inicio, null);

        while (!cola.isEmpty()) {
            Nodo<Integer> actual = cola.poll();

            if (actual == destino) {
                return reconstruirCamino(padres, inicio, destino);
            }

            List<Nodo<Integer>> vecinos = obtenerVecinos(actual);
            for (Nodo<Integer> vecino : vecinos) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    padres.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        return null;
    }

    /**
     * Obtiene todos los venciones de un nodo
     * @param nodo Nodo a evaluar
     * @return Lista de vecinos
     */
    private List<Nodo<Integer>> obtenerVecinos(Nodo<Integer> nodo) {
        List<Nodo<Integer>> vecinos = new ArrayList<>();
        if (nodo.getUpN() != null) vecinos.add(nodo.getUpN());
        if (nodo.getDownN() != null) vecinos.add(nodo.getDownN());
        if (nodo.getLeftN() != null) vecinos.add(nodo.getLeftN());
        if (nodo.getRightN() != null) vecinos.add(nodo.getRightN());
        return vecinos;
    }

    /**
     * Vuelve a contruir el camino de recorrido cada vez
     * @param padres mapa de los nodos
     * @param inicio nodo inicial
     * @param destino nodo destion
     * @return Lista de recorrido
     */
    private List<Nodo<Integer>> reconstruirCamino(Map<Nodo<Integer>, Nodo<Integer>> padres,
                                                  Nodo<Integer> inicio, Nodo<Integer> destino) {
        List<Nodo<Integer>> camino = new ArrayList<>();
        Nodo<Integer> actual = destino;

        while (actual != null) {
            camino.add(0, actual);
            actual = padres.get(actual);
        }

        return camino;
    }

    /**
     * Actualiza el objetivo
     * @param nodo nodo objetivo
     */
    private void moverA(Nodo<Integer> nodo) {
        nodoObjetivo = nodo;
        targetPosition = new Position(nodo.getPosition().getX(), nodo.getPosition().getY());
        state = 1;
    }

    /**
     * Hace que mientras se mueva, no pertenezca a ningun nodo y evitar errores
     *
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

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Nodo<Integer> getNodoObjetivo() {
        return nodoObjetivo;
    }

    public void setNodoObjetivo(Nodo<Integer> nodoObjetivo) {
        this.nodoObjetivo = nodoObjetivo;
    }

    public void setNodoActual(Nodo<Integer> nodoActual) {
        this.nodoActual = nodoActual;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Image getQuieto() {
        return quieto;
    }

    public void setQuieto(Image quieto) {
        this.quieto = quieto;
    }

    public ArrayList<Image> getMoviendo() {
        return moviendo;
    }

    public void setMoviendo(ArrayList<Image> moviendo) {
        this.moviendo = moviendo;
    }

    public Grafo<Integer> getGrafo() {
        return grafo;
    }

    public void setGrafo(Grafo<Integer> grafo) {
        this.grafo = grafo;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}


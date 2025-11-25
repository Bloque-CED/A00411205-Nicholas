package org.example.integradora2ced.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.integradora2ced.model.structures.Grafo;
import org.example.integradora2ced.model.structures.Nodo;

import java.util.*;

public class Enemigo2 {

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

    public Enemigo2(Canvas canvas, Nodo<Integer> nodo, Grafo<Integer> grafo) {
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
        this.quieto = new Image(getClass().getResourceAsStream("/org/example/integradora2ced/enemigo/rojo (2).png"), 15, 15, false, false);

        for (int i = 0; i < 4; i++) {
            moviendo.add(new Image(getClass().getResourceAsStream("/org/example/integradora2ced/enemigo/rojo (" + (i + 1) + ").png"), 15, 15, false, false));
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

        List<Nodo<Integer>> camino = dijkstra(nodoActual, nodoJugador);

        if (camino != null && camino.size() > 1) {
            Nodo<Integer> siguienteNodo = camino.get(1);
            moverA(siguienteNodo);
        }
    }

    /**
     * Implementacion del metodo Dijkstra en el grafo
     * @param inicio nodo inicial
     * @param destino nodo destino
     * @return Lista de todos los nodos del recorrido
     */
    private List<Nodo<Integer>> dijkstra(Nodo<Integer> inicio, Nodo<Integer> destino) {
        Map<Nodo<Integer>, Integer> distancias = new HashMap<>();
        Map<Nodo<Integer>, Nodo<Integer>> padres = new HashMap<>();
        PriorityQueue<NodoDistancia> colaPrioridad = new PriorityQueue<>();
        Set<Nodo<Integer>> visitados = new HashSet<>();

        for (Nodo<Integer> nodo : grafo.getNodos()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(inicio, 0);
        colaPrioridad.add(new NodoDistancia(inicio, 0));
        padres.put(inicio, null);

        while (!colaPrioridad.isEmpty()) {
            NodoDistancia actual = colaPrioridad.poll();
            Nodo<Integer> nodoActual = actual.nodo;

            if (visitados.contains(nodoActual)) {
                continue;
            }

            visitados.add(nodoActual);

            if (nodoActual == destino) {
                return reconstruirCamino(padres, inicio, destino);
            }

            explorarVecino(nodoActual, nodoActual.getUpN(), nodoActual.getUpE() != null ? nodoActual.getUpE().getData() : 1,
                    distancias, padres, colaPrioridad, visitados);
            explorarVecino(nodoActual, nodoActual.getDownN(), nodoActual.getDownE() != null ? nodoActual.getDownE().getData() : 1,
                    distancias, padres, colaPrioridad, visitados);
            explorarVecino(nodoActual, nodoActual.getLeftN(), nodoActual.getLeftE() != null ? nodoActual.getLeftE().getData() : 1,
                    distancias, padres, colaPrioridad, visitados);
            explorarVecino(nodoActual, nodoActual.getRightN(), nodoActual.getRightE() != null ? nodoActual.getRightE().getData() : 1,
                    distancias, padres, colaPrioridad, visitados);
        }

        return null;
    }

    /**
     * Explorar los vecinos para calular los recorrido
     * @param actual nodo actual
     * @param vecino nodo vecino
     * @param peso peso del camino
     * @param distancias distancia entre nodos
     * @param padres Mapa entre los dos nodos
     * @param cola cola de proridad
     * @param visitados cambiar el estado de los visitados
     */
    private void explorarVecino(Nodo<Integer> actual, Nodo<Integer> vecino, int peso,
                                Map<Nodo<Integer>, Integer> distancias,
                                Map<Nodo<Integer>, Nodo<Integer>> padres,
                                PriorityQueue<NodoDistancia> cola,
                                Set<Nodo<Integer>> visitados) {
        if (vecino == null || visitados.contains(vecino)) {
            return;
        }

        int nuevaDistancia = distancias.get(actual) + peso;

        if (nuevaDistancia < distancias.get(vecino)) {
            distancias.put(vecino, nuevaDistancia);
            padres.put(vecino, actual);
            cola.add(new NodoDistancia(vecino, nuevaDistancia));
        }
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

    public Grafo<Integer> getGrafo() {
        return grafo;
    }

    public void setGrafo(Grafo<Integer> grafo) {
        this.grafo = grafo;
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

    /**
     * Clase auxiliar para la cola de prioridad de Dijkstra y compara los nodos segun su distancia
     */
    private static class NodoDistancia implements Comparable<NodoDistancia> {
        Nodo<Integer> nodo;
        int distancia;

        NodoDistancia(Nodo<Integer> nodo, int distancia) {
            this.nodo = nodo;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }
}
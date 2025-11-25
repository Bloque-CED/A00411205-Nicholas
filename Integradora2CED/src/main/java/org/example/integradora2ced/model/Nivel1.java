package org.example.integradora2ced.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import org.example.integradora2ced.model.structures.Edge;
import org.example.integradora2ced.model.structures.Grafo;
import org.example.integradora2ced.model.structures.Nodo;
import org.example.integradora2ced.model.Position;

import java.util.ArrayList;
import java.util.Random;

public class Nivel1 {

    private Canvas canvas;
    private GraphicsContext gc;
    private Grafo<Integer> grafo;
    private Jugador jugador;
    private Enemigo1 enemigo1;
    private Enemigo2 enemigo2;

    private static final int GRID_SIZE = 8;
    private static final double NODE_SPACING = 80;
    private static final double OFFSET_X = 95;
    private static final double OFFSET_Y = 95;
    private Nodo<Integer> nodoInicial;
    private Nodo<Integer> nodoFinal;
    private Image imagenMeta;
    private static final double NODE_SIZE = 30;

    public Nivel1(Canvas canvas) {
        this.canvas = canvas;
        this.gc = this.canvas.getGraphicsContext2D();
        this.grafo = new Grafo<>();
        generarMalla8x8ConPesos();
        this.jugador = new Jugador(this.canvas, nodoInicial);

        // Inicializar enemigos en el nodo final (la meta)
        this.enemigo1 = new Enemigo1(this.canvas, nodoFinal, grafo);
        this.enemigo2 = new Enemigo2(this.canvas, nodoFinal, grafo);

        try {
            this.imagenMeta = new Image(getClass().getResourceAsStream("/org/example/images/laberinto/meta.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de meta");
        }
    }

    /**
     * Pinta el canvas y todos sus componentes del nivel
     */
    public void paint() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        pintarAristas();
        pintarNodos();

        enemigo1.paint();
        enemigo2.paint();

        jugador.paint();
    }

    /**
     * Pinta todas las aristas del grafo con colores según su peso
     */
    private void pintarAristas() {
        gc.setLineWidth(3);

        ArrayList<Edge<Integer>> aristasYaPintadas = new ArrayList<>();

        for (Nodo<Integer> nodo : grafo.getNodos()) {
            Position posActual = nodo.getPosition();
            double centroX = posActual.getX() + NODE_SIZE / 2;
            double centroY = posActual.getY() + NODE_SIZE / 2;

            if (nodo.getRightN() != null && !aristasYaPintadas.contains(nodo.getRightE())) {
                Position posDerecha = nodo.getRightN().getPosition();
                double centroDerechaX = posDerecha.getX() + NODE_SIZE / 2;
                double centroDerechaY = posDerecha.getY() + NODE_SIZE / 2;

                int peso = nodo.getRightE().getData();
                gc.setStroke(getColorPorPeso(peso));
                gc.strokeLine(centroX, centroY, centroDerechaX, centroDerechaY);

                aristasYaPintadas.add(nodo.getRightE());
            }

            if (nodo.getDownN() != null && !aristasYaPintadas.contains(nodo.getDownE())) {
                Position posAbajo = nodo.getDownN().getPosition();
                double centroAbajoX = posAbajo.getX() + NODE_SIZE / 2;
                double centroAbajoY = posAbajo.getY() + NODE_SIZE / 2;

                int peso = nodo.getDownE().getData();
                gc.setStroke(getColorPorPeso(peso));
                gc.strokeLine(centroX, centroY, centroAbajoX, centroAbajoY);

                aristasYaPintadas.add(nodo.getDownE());
            }
        }
    }

    /**
     * Recibe un peso y lo pasa a color para pintar la arisat
     * @param peso peso de la arista de 1 a 3
     * @return Color segun el peso
     */
    private Color getColorPorPeso(int peso) {
        switch (peso) {
            case 1: return Color.rgb(139, 69, 19); // Marrón
            case 2: return Color.GRAY;
            case 3: return Color.YELLOW;
            default: return Color.LIGHTBLUE;
        }
    }

    /**
     * Pinta a los nodos en el canvas distribuidos en una cuadricula
     */
    private void pintarNodos() {
        for (Nodo<Integer> nodo : grafo.getNodos()) {
            Position pos = nodo.getPosition();

            if (nodo == nodoFinal) {
                if (imagenMeta != null) {
                    gc.drawImage(imagenMeta, pos.getX(), pos.getY(), NODE_SIZE, NODE_SIZE);
                } else {
                    gc.setFill(Color.GOLD);
                    gc.fillOval(pos.getX(), pos.getY(), NODE_SIZE, NODE_SIZE);
                }
            } else {
                gc.setFill(Color.DARKGRAY);
                gc.fillOval(pos.getX(), pos.getY(), NODE_SIZE, NODE_SIZE);
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(1);
                gc.strokeOval(pos.getX(), pos.getY(), NODE_SIZE, NODE_SIZE);
            }
        }
    }

    /**
     * Genera una malla de 8x8 con todos los nodos conectados y pesos aleatorios en las aristas
     */
    private void generarMalla8x8ConPesos() {
        Nodo<Integer>[][] matrizNodos = new Nodo[GRID_SIZE][GRID_SIZE];
        Random random = new Random();

        for (int fila = 0; fila < GRID_SIZE; fila++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                double x = OFFSET_X + col * NODE_SPACING;
                double y = OFFSET_Y + fila * NODE_SPACING;
                Position position = new Position(x, y);

                int nodeId = fila * GRID_SIZE + col;
                Nodo<Integer> nodo = new Nodo<>(nodeId, position, this.canvas);

                matrizNodos[fila][col] = nodo;
                grafo.addNodes(nodo);

                if (fila == 0 && col == 0) {
                    nodoInicial = nodo;
                }

                if (fila == GRID_SIZE - 1 && col == GRID_SIZE - 1) {
                    nodoFinal = nodo;
                }
            }
        }

        for (int fila = 0; fila < GRID_SIZE; fila++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Nodo<Integer> nodoActual = matrizNodos[fila][col];

                if (fila > 0) {
                    Nodo<Integer> nodoArriba = matrizNodos[fila - 1][col];
                    int peso = random.nextInt(3) + 1; // 1, 2 o 3
                    Edge<Integer> edgeUp = new Edge<>(nodoActual, peso, nodoArriba);
                    nodoActual.setUpN(nodoArriba);
                    nodoActual.setUpE(edgeUp);
                    grafo.addEdge(nodoActual, nodoArriba, peso);
                }

                if (fila < GRID_SIZE - 1) {
                    Nodo<Integer> nodoAbajo = matrizNodos[fila + 1][col];
                    int peso = random.nextInt(3) + 1;
                    Edge<Integer> edgeDown = new Edge<>(nodoActual, peso, nodoAbajo);
                    nodoActual.setDownN(nodoAbajo);
                    nodoActual.setDownE(edgeDown);
                    grafo.addEdge(nodoActual, nodoAbajo, peso);
                }

                if (col > 0) {
                    Nodo<Integer> nodoIzquierda = matrizNodos[fila][col - 1];
                    int peso = random.nextInt(3) + 1;
                    Edge<Integer> edgeLeft = new Edge<>(nodoActual, peso, nodoIzquierda);
                    nodoActual.setLeftN(nodoIzquierda);
                    nodoActual.setLeftE(edgeLeft);
                    grafo.addEdge(nodoActual, nodoIzquierda, peso);
                }

                if (col < GRID_SIZE - 1) {
                    Nodo<Integer> nodoDerecha = matrizNodos[fila][col + 1];
                    int peso = random.nextInt(3) + 1;
                    Edge<Integer> edgeRight = new Edge<>(nodoActual, peso, nodoDerecha);
                    nodoActual.setRightN(nodoDerecha);
                    nodoActual.setRightE(edgeRight);
                    grafo.addEdge(nodoActual, nodoDerecha, peso);
                }
            }
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public Grafo<Integer> getGrafo() {
        return grafo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Enemigo1 getEnemigo1() {
        return enemigo1;
    }

    public Enemigo2 getEnemigo2() {
        return enemigo2;
    }

    public Nodo<Integer> getNodoFinal() {
        return nodoFinal;
    }
}
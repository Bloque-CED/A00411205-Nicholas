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

public class Nivel2 {

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

    public Nivel2(Canvas canvas) {
        this.canvas = canvas;
        this.gc = this.canvas.getGraphicsContext2D();
        this.grafo = new Grafo<>();
        generarLaberintoConectado8x8();
        this.jugador = new Jugador(this.canvas, nodoInicial);

        // Inicializar enemigos en el nodo final
        this.enemigo1 = new Enemigo1(this.canvas, nodoFinal, grafo);
        this.enemigo2 = new Enemigo2(this.canvas, nodoFinal, grafo);

        try {
            this.imagenMeta = new Image(getClass().getResourceAsStream("/org/example/images/laberinto/meta.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de meta");
        }
    }

    public void paint() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        pintarAristas();
        pintarNodos();

        // Pintar enemigos
        enemigo1.paint();
        enemigo2.paint();

        // Pintar jugador
        jugador.paint();
    }

    private void pintarAristas() {
        gc.setStroke(Color.GRAY);
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
                gc.strokeLine(centroX, centroY, centroDerechaX, centroDerechaY);
                aristasYaPintadas.add(nodo.getRightE());
            }

            if (nodo.getDownN() != null && !aristasYaPintadas.contains(nodo.getDownE())) {
                Position posAbajo = nodo.getDownN().getPosition();
                double centroAbajoX = posAbajo.getX() + NODE_SIZE / 2;
                double centroAbajoY = posAbajo.getY() + NODE_SIZE / 2;
                gc.strokeLine(centroX, centroY, centroAbajoX, centroAbajoY);
                aristasYaPintadas.add(nodo.getDownE());
            }
        }
    }

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
     * Genera un laberinto conectado donde todos los nodos son alcanzables
     */
    private void generarLaberintoConectado8x8() {
        Nodo<Integer>[][] matrizNodos = new Nodo[GRID_SIZE][GRID_SIZE];

        // Crear todos los nodos
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

        // Laberinto conectado con un patrón que garantiza que todos los nodos son alcanzables
        // Peso 1 para todas las aristas
        int[][] conexiones = {
                // Fila 0
                {0,0,1}, {0,1,1}, {0,2,1}, {0,3,2}, {0,4,1}, {0,5,1}, {0,6,1},
                {0,0,2}, {0,2,2}, {0,4,2}, {0,6,2},

                // Fila 1
                {1,0,1}, {1,3,1}, {1,4,1}, {1,6,1},
                {1,1,2}, {1,3,2}, {1,5,2}, {1,7,2},

                // Fila 2
                {2,0,2}, {2,1,1}, {2,2,2}, {2,3,1}, {2,5,1}, {2,6,1},
                {2,4,2}, {2,6,2},

                // Fila 3
                {3,0,1}, {3,1,2}, {3,2,1}, {3,3,2}, {3,4,1}, {3,5,1},
                {3,6,2},

                // Fila 4
                {4,1,1}, {4,2,2}, {4,3,1}, {4,4,2}, {4,5,2}, {4,6,1},
                {4,0,2}, {4,7,2},

                // Fila 5
                {5,0,1}, {5,1,1}, {5,2,1}, {5,4,1}, {5,5,1}, {5,6,1},
                {5,0,2}, {5,2,2}, {5,4,2},

                // Fila 6
                {6,0,1}, {6,1,1}, {6,2,1}, {6,4,1}, {6,5,1}, {6,6,1},
                {6,1,2}, {6,3,2}, {6,5,2},

                // Fila 7
                {7,1,1}, {7,2,1}, {7,3,1}, {7,4,1}, {7,5,1}, {7,6,1}
        };

        // Crear las conexiones
        for (int[] con : conexiones) {
            int fila = con[0];
            int col = con[1];
            int dir = con[2]; // 1=derecha, 2=abajo

            Nodo<Integer> nodoActual = matrizNodos[fila][col];

            if (dir == 1 && col < GRID_SIZE - 1) {
                Nodo<Integer> nodoDerecha = matrizNodos[fila][col + 1];
                conectarNodos(nodoActual, nodoDerecha, true);
            } else if (dir == 2 && fila < GRID_SIZE - 1) {
                Nodo<Integer> nodoAbajo = matrizNodos[fila + 1][col];
                conectarNodos(nodoActual, nodoAbajo, false);
            }
        }
    }

    /**
     * Conecta dos nodos bidireccional mente
     */
    private void conectarNodos(Nodo<Integer> nodo1, Nodo<Integer> nodo2, boolean esHorizontal) {
        int peso = 1; // Todos los vértices valen 1 en el nivel 2

        if (esHorizontal) {
            // Conectar derecha
            Edge<Integer> edgeRight = new Edge<>(nodo1, peso, nodo2);
            nodo1.setRightN(nodo2);
            nodo1.setRightE(edgeRight);
            grafo.addEdge(nodo1, nodo2, peso);

            // Conectar izquierda (inverso)
            Edge<Integer> edgeLeft = new Edge<>(nodo2, peso, nodo1);
            nodo2.setLeftN(nodo1);
            nodo2.setLeftE(edgeLeft);
        } else {
            // Conectar abajo
            Edge<Integer> edgeDown = new Edge<>(nodo1, peso, nodo2);
            nodo1.setDownN(nodo2);
            nodo1.setDownE(edgeDown);
            grafo.addEdge(nodo1, nodo2, peso);

            // Conectar arriba (inverso)
            Edge<Integer> edgeUp = new Edge<>(nodo2, peso, nodo1);
            nodo2.setUpN(nodo1);
            nodo2.setUpE(edgeUp);
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
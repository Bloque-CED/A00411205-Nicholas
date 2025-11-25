package org.example.integradora2ced.model.structures;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.integradora2ced.model.Position;

import java.util.ArrayList;


public class Nodo<T> {

    T data;

    private Position position;

    private Image image;

    private Canvas canvas;
    private GraphicsContext gc;

    private Edge<T> upE;
    private Edge<T> downE;
    private Edge<T> leftE;
    private Edge<T> rightE;

    private Nodo<T> upN;
    private Nodo<T> leftN;
    private Nodo<T> rightN;
    private Nodo<T> downN;

    public Nodo(T data, Position position, Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.data = data;
        this.upE = null;
        this.downE = null;
        this.leftE = null;
        this.rightE = null;
        this.upN = null;
        this.leftN = null;
        this.rightN = null;
        this.downN = null;
        Image imagen = new Image(getClass().getResourceAsStream("/org/example/integradora2ced/laberinto/interseccion.png"),15,15,false,false);
        this.image = imagen;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Edge<T> getUpE() {
        return upE;
    }

    public void setUpE(Edge<T> upE) {
        this.upE = upE;
    }

    public Edge<T> getDownE() {
        return downE;
    }

    public void setDownE(Edge<T> downE) {
        this.downE = downE;
    }

    public Edge<T> getLeftE() {
        return leftE;
    }

    public void setLeftE(Edge<T> leftE) {
        this.leftE = leftE;
    }

    public Edge<T> getRightE() {
        return rightE;
    }

    public void setRightE(Edge<T> rightE) {
        this.rightE = rightE;
    }

    public Nodo<T> getUpN() {
        return upN;
    }

    public void setUpN(Nodo<T> upN) {
        this.upN = upN;
    }

    public Nodo<T> getLeftN() {
        return leftN;
    }

    public void setLeftN(Nodo<T> leftN) {
        this.leftN = leftN;
    }

    public Nodo<T> getRightN() {
        return rightN;
    }

    public void setRightN(Nodo<T> rightN) {
        this.rightN = rightN;
    }

    public Nodo<T> getDownN() {
        return downN;
    }

    public void setDownN(Nodo<T> downN) {
        this.downN = downN;
    }

    /**
     * Informacion del nodo
     * @return Cadena que muestra el contenido del nodo
     */
    @Override
    public String toString() {
        return "Nodo{" +
                "data=" + data +
                ", upE=" + upE +
                ", downE=" + downE +
                ", leftE=" + leftE +
                ", rightE=" + rightE +
                ", upN=" + upN +
                ", leftN=" + leftN +
                ", rightN=" + rightN +
                ", downN=" + downN +
                '}';
    }
}
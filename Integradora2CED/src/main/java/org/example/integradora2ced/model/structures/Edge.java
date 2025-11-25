package org.example.integradora2ced.model.structures;
import org.example.integradora2ced.model.Position;

public class Edge<T>{

    private T data;
    private Nodo<T> nodo1;
    private Nodo<T> nodo2;


    public Edge(Nodo<T> nodo1, T data, Nodo<T> nodo2) {
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.data = data;
    }

    public Nodo<T> getNodo2() {
        return nodo2;
    }
    public void setNodo2(Nodo<T> nodo2) {
        this.nodo2 = nodo2;
    }

    public Nodo<T> getNodo1() {
        return nodo1;
    }
    public void setNodo1(Nodo<T> nodo1) {
        this.nodo1 = nodo1;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Informacion de los vertices
     * @return Cadena que muestra el contenido del vertice
     */
    @Override
    public String toString() {
        return "Edge{" +
                "data=" + data +
                ", nodo1=" + nodo1 +
                ", nodo2=" + nodo2 +
                '}';
    }
}

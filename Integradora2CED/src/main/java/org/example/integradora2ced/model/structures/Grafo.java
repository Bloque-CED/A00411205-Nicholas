package org.example.integradora2ced.model.structures;

import org.example.integradora2ced.model.Position;
import java.util.ArrayList;

public class Grafo<T>{

    private ArrayList<Nodo<T>> nodos;
    private ArrayList<Edge<T>> vertices;

    public Grafo(){
        nodos = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    /**
     * El metodo anade nodos al grafo
     * @param node nodo a anadir
     */
    public void addNodes(Nodo<T> node){
        nodos.add(node);
    }

    /**
     * El metodo anade vertices a la lista
     * @param node1 Nodo origen
     * @param node2 Nodo destino
     * @param peso Peso del vertice
     */
    public void addEdge(Nodo<T> node1, Nodo<T> node2, T peso){
        vertices.add(new Edge<T>(node1,peso,node2));
    }

    /**
     * El metodo elimina nodos de la lista
     * @param node Nodo a eliminar
     */
    public void eraseNode(Nodo<T> node){
        nodos.remove(node);
    }

    /**
     * El metodo elimina un vertice
     * @param edge Vertice a eliminar
     */
    public void eraseEdge(Edge<T> edge){
        nodos.remove(edge);
    }

    public ArrayList<Nodo<T>> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<Nodo<T>> nodos) {
        this.nodos = nodos;
    }

    public ArrayList<Edge<T>> getVertices() {
        return vertices;
    }
    public void setVertices(ArrayList<Edge<T>> vertices) {
        this.vertices = vertices;
    }

    /**
     * Informacion del grafo
     * @return Cadena que muestra la informacion del grafo
     */
    @Override
    public String toString() {
        return "Grafo{" +
                "nodos=" + nodos +
                ", vertices=" + vertices +
                '}';
    }
}

package graph;

import java.util.ArrayList;

public class Graph {
    public int vertices;
    public boolean directed;
    public ArrayList<Edge> edges = new ArrayList<>();
    public ArrayList<ArrayList<Edge>> adj = new ArrayList<>();

    public Graph(int vertices, boolean directed) {
        if (vertices < 1) throw new IllegalArgumentException("Graph must have vertices");
        this.vertices = vertices;
        this.directed = directed;
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int from, int to, int weight) {
        if (from < 0 || to < 0 || from >= vertices || to >= vertices)
            throw new IllegalArgumentException("Invalid vertex");
        edges.add(new Edge(from, to, weight));
        adj.get(from).add(new Edge(from, to, weight));
        if (!directed) adj.get(to).add(new Edge(to, from, weight));
    }
}

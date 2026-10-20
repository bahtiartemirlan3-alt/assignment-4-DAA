import graph.*;
import mst.*;
import connectivity.*;
import shortestpath.Dijkstra;

public class Main {
    public static void main(String[] args) {
        Graph city = new Graph(5, false);
        city.addEdge(0, 1, 4);
        city.addEdge(0, 2, 2);
        city.addEdge(1, 2, 1);
        city.addEdge(1, 3, 5);
        city.addEdge(2, 3, 8);
        city.addEdge(3, 4, 3);

        Kruskal.Result k = Kruskal.run(city);
        Prim.Result p = Prim.run(city);
        System.out.println("KRUSKAL MST cost: " + k.weight + ", connected: " + k.connected);
        System.out.println("PRIM MST cost: " + p.weight + ", connected: " + p.connected);
        for (Edge edge : k.tree)
            System.out.println("MST road: " + edge.from + " -> " + edge.to + ", cost: " + edge.weight);

        Graph roads = new Graph(5, true);
        roads.addEdge(0, 1, 4);
        roads.addEdge(0, 2, 2);
        roads.addEdge(2, 1, 1);
        roads.addEdge(1, 3, 5);
        roads.addEdge(3, 4, 3);
        Dijkstra.Result paths = Dijkstra.run(roads, 0);
        for (int i = 0; i < roads.vertices; i++) {
            if (paths.distance[i] == Dijkstra.INF) System.out.println("District " + i + ": unreachable");
            else System.out.println("District " + i + ": " + paths.distance[i] + " via " + paths.pathTo(i));
        }

        Graph dag = new Graph(4, true);
        dag.addEdge(0, 1, 1); dag.addEdge(0, 2, 1); dag.addEdge(1, 3, 1);
        System.out.println("Topological order: " + TopologicalSort.run(dag).order);
        Graph directed = new Graph(4, true);
        directed.addEdge(0, 1, 1); directed.addEdge(1, 0, 1);
        directed.addEdge(1, 2, 1); directed.addEdge(2, 3, 1);
        System.out.println("SCC groups: " + SCC.run(directed).components);
    }
}

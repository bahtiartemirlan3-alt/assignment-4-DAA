package tests;

import graph.*;
import dsu.DSU;
import mst.*;
import connectivity.*;
import shortestpath.Dijkstra;
import java.util.*;

public class GraphTest {
    static int tests = 0;
    static void check(boolean condition, String description) {
        tests++;
        if (!condition) throw new AssertionError("FAILED: " + description);
        System.out.println("PASS: " + description);
    }

    static boolean validOrder(Graph g, ArrayList<Integer> order) {
        if (order.size() != g.vertices) return false;
        int[] position = new int[g.vertices];
        boolean[] seen = new boolean[g.vertices];
        for (int i = 0; i < order.size(); i++) {
            int u = order.get(i);
            if (seen[u]) return false;
            seen[u] = true;
            position[u] = i;
        }
        for (Edge e : g.edges) if (position[e.from] >= position[e.to]) return false;
        return true;
    }

    public static void main(String[] args) {
        DSU dsu = new DSU(4);
        check(dsu.union(0, 1) && dsu.union(1, 2) && dsu.find(0) == dsu.find(2), "DSU union and find");
        check(!dsu.union(0, 2), "DSU detects repeated connection");

        Graph mst = new Graph(4, false);
        mst.addEdge(0, 1, 1); mst.addEdge(1, 2, 2); mst.addEdge(2, 3, 3);
        mst.addEdge(0, 3, 8); mst.addEdge(0, 2, 4);
        check(Kruskal.run(mst).weight == 6 && Prim.run(mst).weight == 6, "MST algorithms agree");
        check(Kruskal.run(mst).connected && Prim.run(mst).connected, "Connected MST");

        Graph disconnected = new Graph(4, false);
        disconnected.addEdge(0, 1, 5);
        check(!Kruskal.run(disconnected).connected && !Prim.run(disconnected).connected, "Disconnected MST");
        Graph equal = new Graph(3, false);
        equal.addEdge(0, 1, 2); equal.addEdge(1, 2, 2); equal.addEdge(0, 2, 2);
        check(Kruskal.run(equal).weight == 4 && Prim.run(equal).weight == 4, "Duplicate weights and multiple valid MSTs");
        Graph single = new Graph(1, false);
        check(Kruskal.run(single).connected && Prim.run(single).connected && Prim.run(single).weight == 0, "Single vertex");

        Graph dag = new Graph(4, true);
        dag.addEdge(0, 1, 1); dag.addEdge(0, 2, 1); dag.addEdge(1, 3, 1); dag.addEdge(2, 3, 1);
        check(!TopologicalSort.run(dag).hasCycle && validOrder(dag, TopologicalSort.run(dag).order), "Valid topological ordering");
        Graph cyclic = new Graph(3, true);
        cyclic.addEdge(0, 1, 1); cyclic.addEdge(1, 2, 1); cyclic.addEdge(2, 0, 1);
        check(TopologicalSort.run(cyclic).hasCycle, "Cycle detected");

        Graph sccGraph = new Graph(5, true);
        sccGraph.addEdge(0, 1, 1); sccGraph.addEdge(1, 0, 1);
        sccGraph.addEdge(1, 2, 1); sccGraph.addEdge(2, 3, 1);
        sccGraph.addEdge(3, 2, 1); sccGraph.addEdge(3, 4, 1);
        SCC.Result scc = SCC.run(sccGraph);
        boolean[] seen = new boolean[5];
        for (ArrayList<Integer> group : scc.components) for (int v : group) {
            if (seen[v]) throw new AssertionError("Repeated SCC vertex");
            seen[v] = true;
        }
        check(scc.components.size() == 3 && seen[0] && seen[1] && seen[2] && seen[3] && seen[4], "Every vertex belongs to one SCC");

        Graph routes = new Graph(4, true);
        routes.addEdge(0, 1, 2); routes.addEdge(1, 2, 3); routes.addEdge(0, 2, 10);
        Dijkstra.Result shortest = Dijkstra.run(routes, 0);
        check(shortest.distance[2] == 5 && shortest.pathTo(2).equals(Arrays.asList(0, 1, 2)), "Dijkstra shortest distance and path");
        check(shortest.distance[3] == Dijkstra.INF && shortest.pathTo(3).isEmpty(), "Dijkstra unreachable vertex");
        Graph negative = new Graph(2, true);
        negative.addEdge(0, 1, -1);
        boolean rejected = false;
        try { Dijkstra.run(negative, 0); } catch (IllegalArgumentException e) { rejected = true; }
        check(rejected, "Negative weight rejected");
        check(Dijkstra.run(single, 0).distance[0] == 0, "Dijkstra single vertex");
        System.out.println("Passed " + tests + " tests.");
    }
}

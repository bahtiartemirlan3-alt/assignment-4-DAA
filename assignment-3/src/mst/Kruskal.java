package mst;

import graph.*;
import dsu.DSU;
import java.util.*;

public class Kruskal {
    public static class Result {
        public long weight, operations;
        public boolean connected;
        public ArrayList<Edge> tree = new ArrayList<>();
    }

    public static Result run(Graph graph) {
        if (graph.directed) throw new IllegalArgumentException("MST needs an undirected graph");
        Result result = new Result();
        ArrayList<Edge> sorted = new ArrayList<>(graph.edges);
        final long[] comparisons = {0};
        sorted.sort((a, b) -> {
            comparisons[0]++;
            return Integer.compare(a.weight, b.weight);
        });
        DSU dsu = new DSU(graph.vertices);
        long checks = 0;
        for (Edge edge : sorted) {
            checks++;
            if (dsu.union(edge.from, edge.to)) {
                result.weight += edge.weight;
                result.tree.add(edge);
                if (result.tree.size() == graph.vertices - 1) break;
            }
        }
        result.connected = result.tree.size() == graph.vertices - 1;
        result.operations = comparisons[0] + checks + dsu.operations;
        return result;
    }
}

package mst;

import graph.*;
import java.util.*;

public class Prim {
    public static class Result {
        public long weight, operations;
        public boolean connected;
        public ArrayList<Edge> tree = new ArrayList<>();
    }

    public static Result run(Graph graph) {
        if (graph.directed) throw new IllegalArgumentException("MST needs an undirected graph");
        Result result = new Result();
        boolean[] used = new boolean[graph.vertices];
        final long[] comparisons = {0};
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
            comparisons[0]++;
            return Integer.compare(a.weight, b.weight);
        });
        pq.add(new Edge(-1, 0, 0));
        int visited = 0;
        long checks = 0;
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            checks++;
            if (used[edge.to]) continue;
            used[edge.to] = true;
            visited++;
            if (edge.from != -1) {
                result.weight += edge.weight;
                result.tree.add(edge);
            }
            for (Edge next : graph.adj.get(edge.to)) {
                checks++;
                if (!used[next.to]) pq.add(next);
            }
        }
        result.connected = visited == graph.vertices;
        result.operations = checks + comparisons[0];
        return result;
    }
}

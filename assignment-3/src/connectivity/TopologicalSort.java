package connectivity;

import graph.*;
import java.util.*;

public class TopologicalSort {
    public static class Result {
        public ArrayList<Integer> order = new ArrayList<>();
        public boolean hasCycle;
        public long operations;
    }
    public static Result run(Graph graph) {
        if (!graph.directed) throw new IllegalArgumentException("Topological sort needs directed graph");
        Result result = new Result();
        int[] indegree = new int[graph.vertices];
        for (Edge e : graph.edges) { indegree[e.to]++; result.operations++; }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < graph.vertices; i++)
            if (indegree[i] == 0) queue.add(i);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.order.add(node);
            result.operations++;
            for (Edge e : graph.adj.get(node)) {
                result.operations++;
                indegree[e.to]--;
                if (indegree[e.to] == 0) queue.add(e.to);
            }
        }
        result.hasCycle = result.order.size() != graph.vertices;
        if (result.hasCycle) result.order.clear();
        return result;
    }
}

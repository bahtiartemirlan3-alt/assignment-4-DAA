package connectivity;

import graph.*;
import java.util.*;

public class SCC {
    public static class Result {
        public ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        public long operations;
    }
    public static Result run(Graph graph) {
        if (!graph.directed) throw new IllegalArgumentException("SCC needs directed graph");
        Result result = new Result();
        boolean[] visited = new boolean[graph.vertices];
        ArrayList<Integer> order = new ArrayList<>();
        for (int start = 0; start < graph.vertices; start++) {
            if (visited[start]) continue;
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{start, 0});
            visited[start] = true;
            result.operations++;
            while (!stack.isEmpty()) {
                int[] frame = stack.peek();
                int u = frame[0];
                if (frame[1] < graph.adj.get(u).size()) {
                    int v = graph.adj.get(u).get(frame[1]++).to;
                    result.operations++;
                    if (!visited[v]) { visited[v] = true; stack.push(new int[]{v, 0}); }
                } else { order.add(u); stack.pop(); }
            }
        }
        ArrayList<ArrayList<Integer>> reverse = new ArrayList<>();
        for (int i = 0; i < graph.vertices; i++) reverse.add(new ArrayList<>());
        for (Edge e : graph.edges) { reverse.get(e.to).add(e.from); result.operations++; }
        Arrays.fill(visited, false);
        for (int i = order.size() - 1; i >= 0; i--) {
            int start = order.get(i);
            if (visited[start]) continue;
            ArrayList<Integer> component = new ArrayList<>();
            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            visited[start] = true;
            while (!stack.isEmpty()) {
                int u = stack.pop();
                component.add(u);
                result.operations++;
                for (int v : reverse.get(u)) {
                    result.operations++;
                    if (!visited[v]) { visited[v] = true; stack.push(v); }
                }
            }
            result.components.add(component);
        }
        return result;
    }
}

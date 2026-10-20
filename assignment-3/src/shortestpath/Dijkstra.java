package shortestpath;

import graph.*;
import java.util.*;

public class Dijkstra {
    public static final long INF = Long.MAX_VALUE / 4;
    public static class Result {
        public long[] distance;
        public int[] previous;
        public long operations;
        public ArrayList<Integer> pathTo(int target) {
            ArrayList<Integer> path = new ArrayList<>();
            if (distance[target] == INF) return path;
            for (int at = target; at != -1; at = previous[at]) path.add(at);
            Collections.reverse(path);
            return path;
        }
    }

    public static Result run(Graph graph, int source) {
        if (source < 0 || source >= graph.vertices) throw new IllegalArgumentException("Invalid source");
        for (Edge e : graph.edges)
            if (e.weight < 0) throw new IllegalArgumentException("Dijkstra needs non-negative weights");
        Result result = new Result();
        result.distance = new long[graph.vertices];
        result.previous = new int[graph.vertices];
        Arrays.fill(result.distance, INF);
        Arrays.fill(result.previous, -1);
        result.distance[source] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, source});
        while (!pq.isEmpty()) {
            long[] current = pq.poll();
            result.operations++;
            int u = (int) current[1];
            if (current[0] != result.distance[u]) continue;
            for (Edge e : graph.adj.get(u)) {
                result.operations++;
                long newDistance = result.distance[u] + e.weight;
                if (newDistance < result.distance[e.to]) {
                    result.distance[e.to] = newDistance;
                    result.previous[e.to] = u;
                    pq.add(new long[]{newDistance, e.to});
                }
            }
        }
        return result;
    }
}

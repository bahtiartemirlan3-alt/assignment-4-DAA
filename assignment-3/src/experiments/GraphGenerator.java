package experiments;

import graph.Graph;
import java.util.*;

public class GraphGenerator {
    private static long key(int a, int b) { return ((long) a << 32) | (b & 0xffffffffL); }

    public static Graph undirected(int n, int m, long seed) {
        if (m < n - 1 || (long)m > (long)n * (n - 1) / 2) throw new IllegalArgumentException("Invalid edge count");
        Random random = new Random(seed);
        Graph g = new Graph(n, false);
        HashSet<Long> used = new HashSet<>();
        for (int i = 1; i < n; i++) addUndirected(g, used, i, random.nextInt(i), random);
        while (g.edges.size() < m) addUndirected(g, used, random.nextInt(n), random.nextInt(n), random);
        return g;
    }

    private static void addUndirected(Graph g, HashSet<Long> used, int a, int b, Random random) {
        if (a == b) return;
        int x = Math.min(a, b), y = Math.max(a, b);
        if (used.add(key(x, y))) g.addEdge(x, y, 1 + random.nextInt(100));
    }

    public static Graph directed(int n, int m, long seed, boolean dag) {
        if (m < 0 || (long)m > (long)n * (n - 1) / (dag ? 2 : 1)) throw new IllegalArgumentException("Invalid edge count");
        Random random = new Random(seed);
        Graph g = new Graph(n, true);
        HashSet<Long> used = new HashSet<>();
        if (!dag && n >= 3 && m >= 3) {
            for (int[] e : new int[][]{{0, 1}, {1, 2}, {2, 0}}) {
                used.add(key(e[0], e[1])); g.addEdge(e[0], e[1], 1 + random.nextInt(100));
            }
        }
        while (g.edges.size() < m) {
            int a = random.nextInt(n), b = random.nextInt(n);
            if (a == b) continue;
            if (dag && a > b) { int temp = a; a = b; b = temp; }
            if (used.add(key(a, b))) g.addEdge(a, b, 1 + random.nextInt(100));
        }
        return g;
    }
}

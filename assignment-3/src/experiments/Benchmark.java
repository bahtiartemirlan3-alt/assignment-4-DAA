package experiments;

import graph.Graph;
import mst.*;
import connectivity.*;
import shortestpath.Dijkstra;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Benchmark {
    interface Job { long run(); }
    static class Result {
        double ms;
        long operations;
        Result(double ms, long operations) { this.ms = ms; this.operations = operations; }
    }

    static Result measure(Job job) {
        job.run();
        long time = 0, operations = 0;
        for (int repeat = 0; repeat < 5; repeat++) {
            long start = System.nanoTime();
            long count = job.run();
            time += System.nanoTime() - start;
            operations += count;
        }
        return new Result(time / 5.0 / 1_000_000.0, operations / 5);
    }

    static void record(PrintWriter writer, String name, int n, int m, Result r) {
        writer.printf(Locale.US, "%s,%d,%d,%.6f,%d%n", name, n, m, r.ms, r.operations);
        System.out.printf(Locale.US, "%-14s V=%-5d E=%-5d avg=%8.4f ms ops=%d%n", name, n, m, r.ms, r.operations);
    }

    public static void main(String[] args) throws Exception {
        Path output = Paths.get("results", "tables");
        Files.createDirectories(output);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output.resolve("benchmark.csv")))) {
            writer.println("algorithm,vertices,edges,average_ms,average_operations");
            int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
            for (int[] size : sizes) {
                int n = size[0], m = size[1];
                Graph undirected = GraphGenerator.undirected(n, m, 42);
                Graph directed = GraphGenerator.directed(n, m, 42, false);
                Graph dag = GraphGenerator.directed(n, m, 42, true);
                record(writer, "Kruskal", n, m, measure(() -> Kruskal.run(undirected).operations));
                record(writer, "Prim", n, m, measure(() -> Prim.run(undirected).operations));
                record(writer, "SCC", n, m, measure(() -> SCC.run(directed).operations));
                record(writer, "Topo_DAG", n, m, measure(() -> TopologicalSort.run(dag).operations));
                record(writer, "Topo_cycle", n, m, measure(() -> TopologicalSort.run(directed).operations));
                record(writer, "Dijkstra", n, m, measure(() -> Dijkstra.run(directed, 0).operations));
                if (TopologicalSort.run(dag).hasCycle || !TopologicalSort.run(directed).hasCycle)
                    throw new AssertionError("Topological graph generation error");
                if (Kruskal.run(undirected).weight != Prim.run(undirected).weight)
                    throw new AssertionError("MST weights differ");
            }
        }
        System.out.println("Saved results/tables/benchmark.csv");
    }
}

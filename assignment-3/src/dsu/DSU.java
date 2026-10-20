package dsu;

public class DSU {
    private int[] parent, rank;
    public long operations = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        operations++;
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    public boolean union(int a, int b) {
        operations++;
        int rootA = find(a), rootB = find(b);
        if (rootA == rootB) return false;
        if (rank[rootA] < rank[rootB]) parent[rootA] = rootB;
        else if (rank[rootA] > rank[rootB]) parent[rootB] = rootA;
        else { parent[rootB] = rootA; rank[rootA]++; }
        return true;
    }
}

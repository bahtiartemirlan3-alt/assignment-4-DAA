import csv
from pathlib import Path
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

root = Path(__file__).resolve().parents[2]
with (root / 'results/tables/benchmark.csv').open() as file:
    rows = list(csv.DictReader(file))
for field, filename, label in [('vertices', 'time_vs_vertices.png', 'Number of vertices (V)'),
                               ('edges', 'time_vs_edges.png', 'Number of edges (E)')]:
    fig, ax = plt.subplots(figsize=(9, 5))
    for algorithm in dict.fromkeys(row['algorithm'] for row in rows):
        group = [row for row in rows if row['algorithm'] == algorithm]
        ax.plot([int(row[field]) for row in group],
                [float(row['average_ms']) for row in group], marker='o', label=algorithm)
    ax.set_xlabel(label)
    ax.set_ylabel('Average execution time (ms)')
    ax.set_title('Graph algorithms: execution time vs ' + label.lower())
    ax.grid(True, alpha=.3)
    ax.legend()
    fig.tight_layout()
    fig.savefig(root / 'results/plots' / filename, dpi=160)
    plt.close(fig)
print('Saved two plots.')

public class Main {
    public static void main(String[] args) {
        Graph<String> grafo = new Graph<>(true);
        grafo.addEdge("1", "2");
        grafo.addEdge("1", "3");
        grafo.addEdge("3", "2");
        grafo.addEdge("2","3");
        System.out.println(grafo.countVertices());
        System.out.println(grafo.countEdges());
        System.out.println(grafo.calculateSparsity());
       // grafo.loadCSV("JC-202412-citibike-tripdata.csv");
    }
}
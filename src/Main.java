public class Main {
    public static void main(String[] args) {
        Graph<String> grafo = new Graph<>(true);
        grafo.addEdge("1", "2");
        grafo.addEdge("1", "3");
        grafo.addEdge("3", "2");
        System.out.println(grafo);
       // grafo.loadCSV("JC-202412-citibike-tripdata.csv");
        System.out.println(grafo.countVertices());
    }
}
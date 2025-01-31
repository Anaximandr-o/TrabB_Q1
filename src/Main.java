import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {


        Graph<String> grafo = new Graph<>(true);
        grafo.loadCSV("JC-202412-citibike-tripdata.csv");
        System.out.println("Número de estações: " + grafo.countVertices());
        System.out.println("Número de trajetos: " + grafo.countEdges());
        System.out.println("Esparsidade do grafo: " + grafo.calculateSparsity());

        //Questão 3

        Map<String, Integer> grau = grafo.degreeCentrality();
        String estacaoGrau = Collections.max(grau.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Estação mais importante por centralidade de grau: " + estacaoGrau);

    }
}
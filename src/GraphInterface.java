import java.util.ArrayList;
import java.util.Map;
public interface GraphInterface<T> {
    // Cria o grafo a partir de um CSV
    void loadCSV(String filePath);
    // Adiciona vértice no grafo
    void addVertex(T vertex);
    // Adiciona aresta no grafo
    void addEdge(T source, T destination);
    // Retorna verdadeiro se o vértice já existe no grafo
    boolean hasVertex(T vertex);
    // Retorna o número de vértices no grafo
    int countVertices();
    // Retorna o números de arestas no grafo
    int countEdges();
    // Calcula a esparsidade do grafo
    double calculateSparsity();
    // Retorna uma lista de vértices indicando o menor caminho
// entre o vértice start e o vértice end
    ArrayList<T> BFSSearch(T start, T end);
    // Retorna um mapa contendo a distência do vértice
// start a todos os outros do grafo
    Map<T, Integer> DijkstraTraversal(T start);
    // Retorna um mapa contendo a grau de cada vértice do grafo
    Map<T, Integer> degreeCentrality();
    // Retorna um mapa contendo o valor da centralidade de
// proximidade de cada vértice do grafo
    Map<T, Integer> closenessCentrality();
    // Retorna um mapa contendo o valor da centralidade de
// intermediação de cada vértice do grafo
    Map<T, Integer> betweennessCentrality();*/
    // Retorna uma String como representação do grafo
    String toString();
}
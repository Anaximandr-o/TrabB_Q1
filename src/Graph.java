import java.io.File; import java.io.FileNotFoundException; import java.util.*;

public class Graph<T> implements GraphInterface<T>
{
    // We use Hashmap to store the edges in the graph
    public Map<T, List<T>> map = new HashMap<>();
    protected boolean directed = false;

    public Graph(boolean directed)
    {
        this.directed = directed;
    }

    @Override
    public void loadCSV(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            //Ignora header
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                List<String> values = getRecordFromLine(scanner.nextLine());
                this.addEdge((T)values.get(4), (T)values.get(6));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    @Override
    public void addVertex(T vertex)
    {
        map.put(vertex, new LinkedList<T>());
    }

    @Override
    public void addEdge(T source, T destination)
    {
        if (!hasVertex(source))
            addVertex(source);
        if (!hasVertex(destination))
            addVertex(destination);
        if(!map.get(source).contains((destination))){
            map.get(source).add(destination);
            if (!this.directed) {
                map.get(destination).add(source);
            }
        }
    }

    @Override
    public boolean hasVertex(T vertex)
    {
        return map.containsKey(vertex);
    }

    public int countVertices()
    {
        return map.keySet().size();
    }

    public int countEdges()
    {
        int count = 0;
        for(List<T> l : map.values())
            count += l.size();

        return count;
    }

    @Override
    public double calculateSparsity() {
        int n = countVertices();
        return 1 - ((double) countEdges() / (n * n));
    }

    @Override
    public ArrayList<T> BFSSearch(Object start, Object end) {
        int inicio = (Integer) start;
        int fim = (Integer) end;
        Queue<Integer> fila = new LinkedList<>();
        Map<Integer,Integer> predecessor = new HashMap<>(); //Serve para traçar o caminho feito do nó inicio até o fim
        Set<Integer> visitado = new HashSet<>();
        fila.add(inicio);
        visitado.add(inicio);

        while(!fila.isEmpty()){
            int verticeAtual = fila.poll(); //Remove e retorna o primeiro elemento da fila para utilizar no verticeCorrente
            if(verticeAtual == fim)
                return (ArrayList<T>) reconstroiCaminho(predecessor, inicio, fim); //Se chegou ao fim, retorna o caminho feito

            List<Integer> vizinho = (List<Integer>)map.get(verticeAtual); //vizinho recebe uma lista dos vertices adjacentes do vertice atual
            for(int i = 0; i<vizinho.size(); i++){ //O for roda por todos esses vizinhos
                int v = vizinho.get(i);
                if(!visitado.contains(v)){ //O if checa se o vizinho corrente já foi visitado, caso não, adiciona ele nas listas correspondentes;
                    fila.add(v);
                    visitado.add(v);
                    predecessor.put(v, verticeAtual);
                }
            }
        }
        return null;
    }

    private ArrayList<Integer> reconstroiCaminho(Map<Integer, Integer> antecessor, int inicio, int fim){
        ArrayList<Integer> caminho = new ArrayList<>();
        for(Integer i = fim; i != null; i = antecessor.get(i)) //Esse for encontra o caminho percorrido.
            caminho.addFirst(i); //Começando pelo fim, ele é incrementado movendo para o antecessor do vertice corrente
        //Se o vertice não tiver antecessor, ele retorna null.
        return caminho;
    }

    @Override
    public Map<T, Integer> DijkstraTraversal(Object start) {
        int inicio = (Integer) start;
        Map<Integer, Integer> distancias = new HashMap<>();
        PriorityQueue<int[]> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(a -> a[1])); //Cria uma fila de prioridade que armazena int, e organiza eles pela prioridade a[1], ou seja, o primeiro vértice sem ser a raiz

        for (Integer vertice : (Set<Integer>) map.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE); //Determina a distância de todos os vértices até o início como inalcançáveis, seguindo Dijkstra
        }
        distancias.put(inicio, 0);
        filaPrioridade.add(new int[]{inicio, 0});

        while (!filaPrioridade.isEmpty()) { //Roda toda a fila, até que ela esteja vaiza
            int[] atual = filaPrioridade.poll(); //Remove e retorna o vértice de menor custo acumulado
            int verticeAtual = atual[0];
            int custoAtual = atual[1];

            if (custoAtual < distancias.get(verticeAtual)) { //O if faz com que apenas processamos um vértice se não tivermos encontrado um caminho melhor antes
                for (Integer vizinho : (Set<Integer>) map.get(verticeAtual)) { //Percorre todos os vizinhos do vértice atual
                    int novoCusto = custoAtual + 1;
                    if (novoCusto < distancias.get(vizinho)) { //Procura encontrar as menores distâncias possíveis
                        distancias.put(vizinho, novoCusto);
                        filaPrioridade.add(new int[]{vizinho, novoCusto});
                    }
                }
            }
        }
        return (Map<T, Integer>) distancias;
    }

    @Override
    public Map<T, Integer> degreeCentrality() {
        Map<T, Integer> graus = new HashMap<>();

        for (T vertice : map.keySet()) { //Percorre todos os vértices e calcula o grau de centralidade de cada um
            graus.put(vertice, map.get(vertice).size());
        }

        return graus;
    }


    @Override
    public Map<T, Integer> closenessCentrality() {
        Map<T, Integer> centralidade = new HashMap<>();

        for (T vertice : map.keySet()) { //Percorre todos os vértices calculando a centralidade de proximidade
            Map<T, Integer> distancias = (Map<T, Integer>) DijkstraTraversal(vertice); //Utiliza Dijkstra para calcular as distâncias minimas
            int somaDistancias = distancias.values().stream().mapToInt(Integer::intValue).sum(); //Soma todas essas distâncias
            if (somaDistancias > 0) { //Se o vértice tem pelo menos um caminho para outros vértices, a soma das distâncias dele é adicionada ao map
                centralidade.put(vertice, somaDistancias);
            } else {
                centralidade.put(vertice, 0);
            }
        }

        return centralidade;
    }

    @Override
    public Map<T, Integer> betweennessCentrality() {
        Map<T, Integer> centralidade = new HashMap<>();

        for (T vertice : map.keySet()) { //Percorre todos os vértices iniciando a centralidade de intermediação deles como 0
            centralidade.put(vertice, 0);
        }

        for (T vertice : map.keySet()) {
            for (T vertice1 : map.keySet()) { //O duplo for serve para percorrer todas as combinações possíveis de caminhos entre os vértices
                if (!vertice.equals(vertice1)) { //Sendo que o if evita calcular os caminhos de um vértice para ele mesmo
                    List<T> caminho = (List<T>) BFSSearch(vertice, vertice1);  //Usa BFS para encontrar o menor caminho entre os vértices
                    if (caminho != null && caminho.size() > 2) { //Verifica se o caminho encontrado tem pelo menos mais de dois vértices, já que é necessário no minimo 3 para encontrar o intermediário
                        for (int i = 1; i < caminho.size() - 1; i++) { //Roda por todos os vértices intermediários do caminho
                            T intermediario = caminho.get(i);
                            centralidade.put(intermediario, centralidade.get(intermediario).intValue() + 1); //Incrementa a centralidade com o vértice intermediário
                        }
                    }
                }
            }
        }

        return centralidade;
    }

    @Override
    public String toString()
    {
        String text = "";
        System.out.println(map.keySet());
        for(T vertex: map.keySet())
        {
            text += vertex + ": ";
            text += map.get(vertex).toString();
            text += "\n";
        }
        return text;
    }
}
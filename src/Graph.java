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
    public ArrayList<Integer> BFSSearch(Object start, Object end) {
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
                return reconstroiCaminho(predecessor, inicio, fim); //Se chegou ao fim, retorna o caminho feito

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
    public Map DijkstraTraversal(Object start) {
        return Map.of();
    }

    @Override
    public Map degreeCentrality() {
        return Map.of();
    }

    @Override
    public Map closenessCentrality() {
        return Map.of();
    }

    @Override
    public Map betweennessCentrality() {
        return Map.of();
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

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TraWordSal {
    public static void main(String[] args) {
//        TODO: Or use http://www.gwicks.net/dictionaries.htm?
//        Could offload pathfinding to database (neo4j seems decent)
//        Maybe grab a "10.000 most common words" list and search that first before going to ALL words? (more desirable results avoiding words the user doesn't know)
        var graphs = DictionaryLoader.buildGraphFromJson("data/graph_of_words_alpha.json");

//        var graphs = DictionaryLoader.buildGraphFromTxt("data/words_alpha.txt");
//        DictionaryLoader.storeGraphs(graphs, "data/graph_of_words_alpha.json");

        var graph = graphs.get(4);
//        graph.forEach((key, value) -> System.out.println(value.toFullString()));

//        System.out.println("args: " + Arrays.toString(args));

        performSearch(graph, "Hand", "Bank");
        performSearch(graph, "Hand", "Sure");
    }

    public static void performSearch(HashMap<String, WordNode> graph, String startWord, String endWord) {
        PathFinder pathFinder = new PathFinder(graph);
        System.out.println("Searching for " + startWord + " -> " + endWord);
        List<WordNode> path = pathFinder.aStarSearch(startWord, endWord);

        if (path != null) {
            var pathString = path.stream().map(node -> node.word).collect(Collectors.joining(" -> "));
            System.out.println("Found path: [" + pathString + "]");
        } else {
            System.out.println("No path available");
        }
    }
}
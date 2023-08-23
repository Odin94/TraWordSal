import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraWordSal {
    public static void main(String[] args) {
        // Try Hand -> Bank (should be Hand -> Band -> Bank)
//        TODO: Grab dictionary (eg https://github.com/dwyl/english-words) and store in a graph db?
//        Could offload pathfinding to database (neo4j seems decent)
//        Maybe grab a "10.000 most common words" list and search that first before going to ALL words? (more desirable results avoiding words the user doesn't know)
        List<String> dictionary = Arrays.asList(
                "Band",
                "Hand",
                "Sand",
                "Bank",
                "Sure"
        );

        HashMap<String, WordNode> graph = buildGraph(dictionary);

        graph.forEach((key, value) -> System.out.println(value.toFullString()));

        System.out.println("args: " + Arrays.toString(args));

        performSearch(graph, "Hand", "Bank");
        performSearch(graph, "Hand", "Sure");
    }

    public static void performSearch(HashMap<String, WordNode> graph, String startWord, String endWord) {
        PathFinder pathFinder = new PathFinder(graph);
        System.out.println("Searching for " + startWord + " -> " + endWord);
        List<WordNode> path = pathFinder.aStarSearch(startWord, endWord);

        if (path != null) {
            path.stream().map(wordNode -> wordNode.word).forEach(System.out::println);
        } else {
            System.out.println("No path available");
        }
    }

    // Assumes all words in dictionary have same length
    public static HashMap<String, WordNode> buildGraph(List<String> dictionary) {
        HashMap<String, WordNode> graph = new HashMap<>();


//        ls = [1, 2, 3]
//        int[] lsArr = new int[]{1,2,3};
//        ArrayList<int> ls = new ArrayList<>(Arrays.asList(lsArr))

        for (String word : dictionary) {
            WordNode wordNode = new WordNode(word);
            graph.put(word, wordNode);
        }

        graph.forEach((word, wordNode) -> {
            // Can't nest forEach loops because Java is old
            for (Map.Entry<String, WordNode> entry : graph.entrySet()) {
                String otherWord = entry.getKey();
                WordNode otherWordNode = entry.getValue();

                if (word.equals(otherWord)) {
                    continue;
                }

                if (Utils.levenshteinDistance(otherWord, word) == 1) {
                    wordNode.neighbours.add(otherWordNode);
                }
            }
        });

        return graph;
    }
}
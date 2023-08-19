import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraWord {
    public static void main(String[] args) {
        // Try Hand -> Bank (should be Hand -> Band -> Bank)
        List<String> dictionary = Arrays.asList(
                "Band",
                "Hand",
                "Sand",
                "Bank",
                "Sure"
        );

        HashMap<String, WordNode> graph = buildGraph(dictionary);

        graph.forEach((key, value) -> {
            Object[] neighborWordArray = value.neighbours.stream().map(wordNode -> wordNode.word).toArray();
            String neighborWordsString = Arrays.toString(neighborWordArray);
            System.out.println(key + " -> " + neighborWordsString);
        });

        System.out.println("args: " + Arrays.toString(args));

        performSearch(graph, "Hand", "Bank");
        performSearch(graph, "Hand", "Sure");
    }

    public static void performSearch(HashMap<String, WordNode> graph, String startWord, String endWord) {
        PathFinder pathFinder = new PathFinder(graph);
        System.out.println("Searching for " + startWord + " -> " + endWord);
        List<WordNode> path = pathFinder.findPath(graph.get(startWord), endWord);

        if (path != null) {
            path.stream().map(wordNode -> wordNode.word).forEach(System.out::println);
        } else {
            System.out.println("No path available");
        }
    }

    // Assumes all words in dictionary have same length
    public static HashMap<String, WordNode> buildGraph(List<String> dictionary) {
        HashMap<String, WordNode> graph = new HashMap<>();

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

                if (levenshteinDistance(otherWord, word) == 1) {
                    wordNode.neighbours.add(otherWordNode);
                }
            }
        });

        return graph;
    }

    // Assumes x and y are non-empty strings of same length (could assert this and
    // throw if precondition is violated)
    static int levenshteinDistance(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        try {
            int substitution = levenshteinDistance(x.substring(1), y.substring(1))
                    + costOfSubstitution(x.charAt(0), y.charAt(0));

            return substitution;
        } catch(Exception e) {
            System.err.println("Words: " + x + ", " + y);
            throw e;
        }
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
}
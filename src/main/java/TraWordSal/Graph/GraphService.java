package TraWordSal.Graph;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import TraWordSal.Graph.Entities.GetWordsResponse;

@Service
public class GraphService {
    private static HashMap<Integer, HashMap<String, WordNode>> graphs;

    static {
        var path = Path.of(new ClassPathResource("src/main/resources/data/graph_of_words_alpha.json").getPath());
        graphs = DictionaryLoader.buildGraphFromJson(path);
    }

    public List<String> findPath(String startWord, String endWord, boolean onlyEasyPaths) {
        if (!graphs.containsKey(startWord.length())) {
            throw new RuntimeException("No graph for word length " + startWord.length());
        }
        var pathFinder = new PathFinder(graphs.get(startWord.length()));

        var path = pathFinder.aStarSearch(startWord, endWord, onlyEasyPaths);

        if (path != null) {
            return path.stream()
                    .map(node -> node.word)
                    .toList();
        }

        return null;
    }

    public List<String> getNeighbours(String word) {
        if (!graphs.containsKey(word.length())) {
            throw new RuntimeException("No graph for word length " + word.length());
        }

        var wordNode = graphs.get(word.length()).get(word);

        return wordNode.neighbours.stream()
                .map(node -> node.word)
                .toList();
    }

    public GetWordsResponse getWords() {
        var graph = graphs.get(4);
        var words = graph.keySet().stream().toList();

        int randomNumStart = Utils.rnd(0, words.size() - 1);
        int randomNumEnd = Utils.rnd(0, words.size() - 1);

        while (randomNumStart == randomNumEnd) {
            randomNumStart = Utils.rnd(0, words.size() - 1);
        }

        var startWord = words.get(randomNumStart);
        var endWord = words.get(randomNumEnd);
        var startWordNeighbours = graph.get(startWord).neighbours.stream().map(n -> n.word).toList();

        System.out.println("Finding path for: " + startWord + " -> " + endWord);

        var path = findPath(startWord, endWord, true);
        if (path == null) {
            return getWords();
        } else {
            return new GetWordsResponse(startWord, endWord, startWordNeighbours);
        }
    }
}

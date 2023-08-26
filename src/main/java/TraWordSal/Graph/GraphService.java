package TraWordSal.Graph;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

@Service
public class GraphService {
    private static HashMap<Integer, HashMap<String, WordNode>> graphs;

    static {
        var path = Path.of(new ClassPathResource("src/main/resources/data/graph_of_words_alpha.json").getPath());
        graphs = DictionaryLoader.buildGraphFromJson(path);
    }

    public List<String> getPath(String startWord, String endWord) {
        if (!graphs.containsKey(startWord.length())) {
            throw new RuntimeException("No graph for word length " + startWord.length());
        }
        var pathFinder = new PathFinder(graphs.get(startWord.length()));

        return pathFinder.aStarSearch(startWord, endWord).stream()
                .map(node -> node.word)
                .toList();
    }
}

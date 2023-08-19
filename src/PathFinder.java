import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PathFinder {
    public HashMap<String, WordNode> graph;
    public HashSet<WordNode> encounteredNodes;

    public PathFinder(HashMap<String, WordNode> graph) {
        this.graph = graph;
        this.encounteredNodes = new HashSet<>();
    }

//    TODO: Write public wrapper that takes 2 strings, then grabs node and calls this
//    TODO: Optimize (A*?) and/or find optimal solution (currently finds a solution based on order of neighbors)
//    Optimal solution approach: DFS, get a list of recursing over all neighbors, then return the shortest list that isn't null
    public List<WordNode> findPath(WordNode wordNode, String endWord) {
        if (encounteredNodes.contains(wordNode)) {
            return null;
        }
        encounteredNodes.add(wordNode);

        for (WordNode neighbour : wordNode.neighbours) {
            if (encounteredNodes.contains(neighbour)) {
                continue;
            }
            if (neighbour.word.equals(endWord)) {
                return List.of(wordNode, graph.get(endWord));
            }

            List<WordNode> path = findPath(neighbour, endWord);

            if (path == null) {
                return null;
            }

//            TODO: Refactor this
            List<WordNode> newList = new ArrayList<>();
            newList.add(wordNode);
            newList.addAll(path);

            return newList;
        }

        return null;
    }
}

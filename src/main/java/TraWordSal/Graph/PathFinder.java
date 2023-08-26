package TraWordSal.Graph;

import java.util.ArrayList;
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

    public List<WordNode> aStarSearch(String startWord, String endWord) {
        String capitalizedStartWord = Utils.capitalize(startWord);
        String capitalizedEndWord = Utils.capitalize(endWord);

        WordNode startNode = graph.get(capitalizedStartWord);
        if (startNode == null) {
            throw new RuntimeException("Start word '" + capitalizedStartWord + "' is not in dictionary");
        }
        WordNode endNode = graph.get(capitalizedEndWord);
        if (endNode == null) {
            throw new RuntimeException("End word '" + capitalizedEndWord + "' is not in dictionary");
        }

        return aStarSearch(startNode, endWord);
    }

    private List<WordNode> reconstructPath(WordNode current) {
        ArrayList<WordNode> path = new ArrayList<>();
        path.add(current);

        while (current.parent != null) {
            current = current.parent;
            path.add(0, current);
        }

        resetGraph();
        return path;
    }

    private List<WordNode> aStarSearch(WordNode startNode, String endWord) {
        startNode.cost = 0;
        ArrayList<WordNode> openSet = new ArrayList<>();
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            WordNode current = openSet.stream().min((lNeighbour, rNeighbour) -> {
                int lNeighbourScore = getHeuristic(lNeighbour, endWord) + lNeighbour.cost;
                int rNeighbourScore = getHeuristic(rNeighbour, endWord) + rNeighbour.cost;

                return lNeighbourScore - rNeighbourScore;
            }).get();

            if (current.word.equals(endWord)) {
                return reconstructPath(current);
            }

            openSet.remove(current);
            current.neighbours.forEach(n -> {
                int tempCost = current.cost + 1;
                if (tempCost < n.cost) {
                    n.parent = current;
                    n.cost = tempCost;
                }

                if (!openSet.contains(n)) {
                    openSet.add(n);
                }
            });
        }

        resetGraph();
        return null;
    }

    private int getHeuristic(WordNode wordNode, String endWord) {
        return Utils.levenshteinDistance(wordNode.word, endWord);
    }

    public void resetGraph() {
        graph.forEach((key, value) -> {
            value.cost = Integer.MAX_VALUE;
            value.parent = null;
        });
    }
}
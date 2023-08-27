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

    public List<WordNode> aStarSearch(String startWord, String endWord, boolean onlyEasyPaths) {
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

        return aStarSearch(startNode, endWord, onlyEasyPaths);
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

    private List<WordNode> aStarSearch(WordNode startNode, String endWord, boolean onlyEasyPaths) {
        startNode.cost = 0;
        ArrayList<WordNode> openSet = new ArrayList<>();
        openSet.add(startNode);

        var takenSteps = 0;
        var limit = onlyEasyPaths ? 3000 : 20000;
        while (!openSet.isEmpty()) {
//            TODO: There is some issue with the algorithm where things sometimes loop for a while, eg. with Fumy -> Tosh
//            Maybe caused by multiple requests on the same graph causing issues? (only resets at the end, shouldn't handle two at once with the same data structure - make service create a new graph for each request?)
            if (onlyEasyPaths && takenSteps > limit) {
                return null;
            }
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

            takenSteps++;
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
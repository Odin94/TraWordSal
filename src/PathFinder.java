import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class PathFinder {
    public HashMap<String, WordNode> graph;
    public HashSet<WordNode> encounteredNodes;

    public PathFinder(HashMap<String, WordNode> graph) {
        this.graph = graph;
        this.encounteredNodes = new HashSet<>();
    }

    public List<WordNode> findPath(String startWord, String endWord) {
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

        return _findPath(startNode, capitalizedEndWord);
    }


    //    TODO: Optimize (A*?) and/or find optimal solution (currently finds a solution based on order of neighbors)
    private ArrayList<WordNode> _findPath(WordNode wordNode, String endWord) {
        if (encounteredNodes.contains(wordNode)) {
            return null;
        }
        encounteredNodes.add(wordNode);

        List<ArrayList<WordNode>> paths = wordNode.neighbours.stream()
                .map(neighbour -> {
//                    TODO: Oops, doesntImproveDistance actually makes it not find a solution sometimes. Just do A* with levenshtein heuristics
                    if (encounteredNodes.contains(neighbour) || doesntImproveDistance(neighbour.word, wordNode.word, endWord)) {
                        return null;
                    }
                    if (neighbour.word.equals(endWord)) {
                        WordNode[] arr = new WordNode[]{wordNode, graph.get(endWord)};
                        return new ArrayList<>(Arrays.asList(arr));
                    }

                    ArrayList<WordNode> path = _findPath(neighbour, endWord);

                    if (path == null) {
                        return null;
                    }

                    path.add(0, wordNode);
                    return path;
                })
                .filter(Objects::nonNull)
                .toList();

//        System.out.println("---- Found Paths ----");
//        paths.forEach(path -> {
//            for (WordNode node : path) {
//                System.out.print(node + " -> ");
//            }
//        });
//        System.out.println("---- End Found Paths ----");

        Comparator<ArrayList<WordNode>> listSizeComp = Comparator.comparingInt(List::size);
        return paths.stream().min(listSizeComp).orElse(null);
    }

    private boolean doesntImproveDistance(String newWord, String oldWord, String targetWord) {
        return Utils.levenshteinDistance(newWord, targetWord) >= Utils.levenshteinDistance(oldWord, targetWord);
    }

    public ArrayList<WordNode> aStarSearch(String startWord, String endWord) {
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

        return aStarSearch(startNode, endWord, new ArrayList<>(), new ArrayList<>(), 1);
    }

    private ArrayList<WordNode> aStarSearch(WordNode wordNode, String endWord, ArrayList<AStarNode> availableNodes, ArrayList<WordNode> visitedNodes, int depth) {
        if (wordNode == null) return null;
        if (Objects.equals(wordNode.word, endWord)) return new ArrayList<>(List.of(wordNode));

        System.out.println("Looking at " + wordNode.word);
        System.out.println("Available: ");
        availableNodes.stream().map(node -> node.node.word).forEach(System.out::println);
        System.out.println("-----");

        System.out.println("Visited: ");
        visitedNodes.stream().map(node -> node.word).forEach(System.out::println);
        System.out.println("-----");


        wordNode.neighbours.forEach(neighbour -> {
            if (!visitedNodes.contains(neighbour) && availableNodes.stream().filter(n -> n.node == neighbour).findFirst().isEmpty()) {
                availableNodes.add(new AStarNode(neighbour, depth));
            }
        });

        ArrayList<WordNode> path = null;
        while (path == null && !availableNodes.isEmpty()) {
            AStarNode minScoreNode = availableNodes.stream().min((lNeighbour, rNeighbour) -> {
                int lNeighbourScore = getHeuristic(lNeighbour.node, endWord) + lNeighbour.depth;
                int rNeighbourScore = getHeuristic(rNeighbour.node, endWord) + rNeighbour.depth;

                return lNeighbourScore - rNeighbourScore;
            }).orElse(null);

            availableNodes.remove(minScoreNode);
            visitedNodes.add(minScoreNode.node);

            ArrayList<WordNode> more = aStarSearch(minScoreNode.node, endWord, availableNodes, visitedNodes, depth + 1);
            if (more != null) {
                path = new ArrayList<>(List.of(wordNode));
                path.addAll(more);
            }
        }

        return path;
    }

    private int getHeuristic(WordNode wordNode, String endWord) {
        return Utils.levenshteinDistance(wordNode.word, endWord);
    }

    private static class AStarNode {
        public WordNode node;
        public int depth;

        public AStarNode(WordNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }
}
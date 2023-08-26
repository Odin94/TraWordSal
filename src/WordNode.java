import java.util.ArrayList;
import java.util.List;

public class WordNode {
    public String word;
    public ArrayList<WordNode> neighbours;
    public WordNode parent;
    public int cost;

    public WordNode(String word, WordNode parent, ArrayList<WordNode> neighbours, int cost) {
        this.word = word;
        this.parent = parent;
        this.neighbours = neighbours;
        this.cost = cost;
    }

    public WordNode(String word) {
        this.word = word;
        this.parent = null;
        this.neighbours = new ArrayList<>();
        this.cost = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return word;
    }

    public String toFullString() {
        List<String> neighbourWords = neighbours.stream().map(n -> n.word).toList();
        return word + ": [" + String.join(", ", neighbourWords) + "]";
    }
}

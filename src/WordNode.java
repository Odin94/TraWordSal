import java.util.ArrayList;
import java.util.List;

public class WordNode {
    public String word;
    public ArrayList<WordNode> neighbours;

    public WordNode(String word, ArrayList<WordNode> neighbours) {
        this.word = word;
        this.neighbours = neighbours;
    }

    public WordNode(String word) {
        this.word = word;
        this.neighbours = new ArrayList<WordNode>();
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

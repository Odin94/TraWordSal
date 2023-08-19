import java.util.ArrayList;

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
}

package TraWordSal.Graph.Entities;

import java.util.List;

public class GetWordsResponse {
    public String startWord;
    public String endWord;
    public List<String> startWordNeighbours;

    public GetWordsResponse(String startWord, String endWord, List<String> startWordNeighbours) {
        this.startWord = startWord;
        this.endWord = endWord;
        this.startWordNeighbours = startWordNeighbours;
    }
}

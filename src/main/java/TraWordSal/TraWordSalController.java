package TraWordSal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import TraWordSal.Graph.Entities.GetWordsResponse;
import TraWordSal.Graph.GraphService;
import TraWordSal.Graph.Utils;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TraWordSalController {
    @Autowired
    GraphService graphService;

    @GetMapping("/getPath")
    public List<String> getPath(@RequestParam(value = "startWord") String startWord, @RequestParam(value = "endWord") String endWord) {
        if (startWord == null || endWord == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both 'startWord' and 'endWord' request params are required");
        }
        if (startWord.length() != endWord.length()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "'startWord' and 'endWord' must have the same length");
        }

        System.out.println("Getting path for " + startWord + " -> " + endWord);

        var path = graphService.findPath(Utils.capitalize(startWord), Utils.capitalize(endWord), false);
        System.out.println("Got path: " + path);
        return path;
    }

    @GetMapping("/getNeighbours")
    public List<String> getPath(@RequestParam(value = "word") String word) {
        if (word == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'word' request param is required");
        }

        System.out.println("Getting neighbours for " + word);

        var neighbours = graphService.getNeighbours(word);
        System.out.println("Got neighbours: " + neighbours);
        return neighbours;
    }

    @GetMapping("/getWords")
    public GetWordsResponse getWords() {
        System.out.println("Getting words");

        var wordsResponse = graphService.getWords();
        System.out.println("Got words: " + wordsResponse.startWord + " -> " + wordsResponse.endWord);

        return wordsResponse;
    }
}

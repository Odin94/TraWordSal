package TraWordSal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

        var path = graphService.getPath(Utils.capitalize(startWord), Utils.capitalize(endWord));
        System.out.println("Got path: " + path);
        return path;
    }
}

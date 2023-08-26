import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryLoader {

    //    Expects text file formatted with \n separated words
    //    Returns map of wordLength -> (word -> neighbours)
    public static HashMap<Integer, HashMap<String, WordNode>> buildGraphFromTxt(String filePath) { // "data/words_alpha.txt"
        List<String> dictionary;
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            dictionary = stream.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return buildGraphs(dictionary);
    }

    //    Expects json file formatted like {"1": {"A": {"word": "A", "neighbours": ["B","C","D"] } }, "B": ..., } "2": ... }
    public static HashMap<Integer, HashMap<String, WordNode>> buildGraphFromJson(String filePath) {
        HashMap<Integer, HashMap<String, JsonNode>> jsonGraphs;
        try {
            String jsonString = Files.readString(Paths.get(filePath));
            jsonGraphs = new ObjectMapper().readValue(jsonString, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<Integer, HashMap<String, WordNode>> graphs = new HashMap<>();
//        Add WordNodes without neighbours
        jsonGraphs.forEach((size, jsonGraph) -> {
            if (!graphs.containsKey(size)) graphs.put(size, new HashMap<>());

            for (Map.Entry<String, JsonNode> entry : jsonGraph.entrySet()) {
                var jsonNode = entry.getValue();
                var node = new WordNode(jsonNode.word);

                graphs.get(size).put(entry.getKey(), node);
            }
        });

//        Add neighbours
        var max = Collections.max(graphs.keySet());
        for (int i = 1; i <= max; i++) {
            if (!graphs.containsKey(i)) continue;

            var graph = graphs.get(i);
            var jsonGraph = jsonGraphs.get(i);

            for (Map.Entry<String, WordNode> entry : graph.entrySet()) {
                var node = entry.getValue();
                node.neighbours = jsonGraph.get(entry.getKey()).neighbours.stream()
                        .map(graph::get)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        return graphs;
    }

    public static void storeGraphs(HashMap<Integer, HashMap<String, WordNode>> graphs, String filePath) {
        var jsonGraphs = new HashMap<Integer, HashMap<String, JsonNode>>();
        graphs.forEach((size, graph) -> {
            if (!jsonGraphs.containsKey(size)) jsonGraphs.put(size, new HashMap<>());

            for (Map.Entry<String, WordNode> entry : graph.entrySet()) {
                WordNode node = entry.getValue();
                JsonNode jsonNode = new JsonNode(node.word, node.neighbours.stream()
                        .map(n -> n.word)
                        .collect(Collectors.toCollection(ArrayList::new))
                );

                jsonGraphs.get(size).put(entry.getKey(), jsonNode);
            }
        });

        try {
            String json = new ObjectMapper().writeValueAsString(jsonGraphs);
            Files.writeString(Paths.get(filePath), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HashMap<Integer, HashMap<String, WordNode>> buildGraphs(List<String> dictionary) {
        HashMap<Integer, HashMap<String, WordNode>> graphs = new HashMap<>();

        for (String word : dictionary) {
            int wordLength = word.length();
            if (wordLength > 4) continue;

            String capitalizedWord = Utils.capitalize(word);
            WordNode wordNode = new WordNode(capitalizedWord);


            if (!graphs.containsKey(wordLength)) {
                graphs.put(wordLength, new HashMap<>());
            }
            graphs
                    .get(wordLength)
                    .put(capitalizedWord, wordNode);
        }

        graphs.forEach((size, graph) -> {
            System.out.println("Doing " + size);
            for (Map.Entry<String, WordNode> outerEntry : graph.entrySet()) {
                WordNode node = outerEntry.getValue();

                // Can't nest forEach loops because Java is old
                for (Map.Entry<String, WordNode> innerEntry : graph.entrySet()) {
                    WordNode potentialNeighbour = innerEntry.getValue();

                    if (node.equals(potentialNeighbour)) {
                        continue;
                    }

                    if (Utils.levenshteinDistance(node.word, potentialNeighbour.word) == 1) {
                        node.neighbours.add(potentialNeighbour);
                    }
                }
            }
        });

        return graphs;
    }

    private static class JsonNode {
        public String word;
        public ArrayList<String> neighbours;

        public JsonNode(String word, ArrayList<String> neighbours) {
            this.word = word;
            this.neighbours = neighbours;
        }

        public JsonNode() {
        }
    }
}

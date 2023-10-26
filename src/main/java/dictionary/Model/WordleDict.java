package dictionary.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordleDict {
    private String targetWord;
    public List<String> wordList;

    public WordleDict() {
        wordList = readStringListFromFile("src/main/resources/words.txt");
        targetWord = "hello";
    }

    public List<String> readStringListFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTargetWord() {
        return targetWord;
    }
}

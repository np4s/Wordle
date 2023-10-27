package dictionary.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordleDict {
    private String targetWord;
    private List<String> wordList;
    private List<String> targetList;

    public WordleDict() {
        wordList = readStringListFromFile("src/main/resources/words.txt");
        targetList = readStringListFromFile("src/main/resources/target-words.txt");
        randomTargetWord();
    }

    private List<String> readStringListFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void randomTargetWord() {
        int targetIndex = (int) Math.floor(Math.random() * (targetList.size() - 1) + 1);
        targetWord = targetList.get(targetIndex);
        System.out.println(targetWord);
    }

    public String getTargetWord() {
        return targetWord;
    }

    public boolean isValid(String guessWord) {
        for (String word : wordList) {
            if (word.equals(guessWord)) {
                return true;
            }
        }
        for (String word : targetList) {
            if (word.equals(guessWord)) {
                return true;
            }
        }
        return false;
    }
}

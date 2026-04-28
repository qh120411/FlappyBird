import java.util.Arrays;
import java.util.List;

public class Question {

    private final Difficulty difficulty;
    private final String prompt;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    public Question(Difficulty difficulty, String prompt, String[] options, int correctIndex, String explanation) {
        this.difficulty = difficulty;
        this.prompt = prompt;
        this.options = Arrays.asList(options);
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getExplanation() {
        return explanation;
    }
}

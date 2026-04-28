import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizManager {

    private static final QuizManager INSTANCE = new QuizManager();

    private final QuestionFileManager fileManager;
    private final List<Question> questions;
    private final Random random;

    private QuizManager() {
        fileManager = new QuestionFileManager();
        questions = new ArrayList<Question>();
        random = new Random();
        reload();
    }

    public static QuizManager getInstance() {
        return INSTANCE;
    }

    public void reload() {
        questions.clear();
        questions.addAll(fileManager.loadQuestions("data/questions.csv"));
    }

    public Question nextQuestion(Difficulty difficulty) {
        Difficulty safeDifficulty = difficulty == null ? Difficulty.MEDIUM : difficulty;
        List<Question> pool = new ArrayList<Question>();

        for (Question question : questions) {
            if (question.getDifficulty() == safeDifficulty) {
                pool.add(question);
            }
        }

        if (pool.isEmpty()) {
            pool.addAll(questions);
        }

        if (pool.isEmpty()) {
            return new Question(
                Difficulty.MEDIUM,
                "1 + 1 bang may?",
                new String[] { "1", "2", "3", "4" },
                1,
                "1 + 1 = 2."
            );
        }

        return pool.get(random.nextInt(pool.size()));
    }

    public List<Question> getAllQuestions() {
        return Collections.unmodifiableList(questions);
    }
}

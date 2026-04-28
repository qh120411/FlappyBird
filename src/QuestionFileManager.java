import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuestionFileManager {

    public static final String HEADER = "difficulty,question,option_a,option_b,option_c,option_d,correct,explanation";

    public List<Question> loadQuestions(String path) {
        List<Question> questions = new ArrayList<Question>();
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            return getFallbackQuestions();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            boolean isFirstRow = true;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> columns = CsvUtil.parseCsvLine(line);
                if (isFirstRow && looksLikeHeader(columns)) {
                    isFirstRow = false;
                    continue;
                }
                isFirstRow = false;

                if (columns.size() < 8) {
                    continue;
                }

                Difficulty difficulty = Difficulty.fromString(columns.get(0).trim());
                String prompt = columns.get(1).trim();
                String[] options = new String[] {
                    columns.get(2).trim(),
                    columns.get(3).trim(),
                    columns.get(4).trim(),
                    columns.get(5).trim()
                };
                int correctIndex = parseCorrectIndex(columns.get(6).trim());
                String explanation = columns.get(7).trim();

                if (!prompt.isEmpty() && correctIndex >= 0 && correctIndex < options.length) {
                    questions.add(new Question(difficulty, prompt, options, correctIndex, explanation));
                }
            }
        } catch (IOException e) {
            System.err.println("Khong doc duoc file cau hoi: " + e.getMessage());
        }

        return questions.isEmpty() ? getFallbackQuestions() : questions;
    }

    public void saveQuestions(String path, List<Question> questions) {
        List<String> lines = new ArrayList<String>();
        lines.add(HEADER);

        for (Question question : questions) {
            StringBuilder row = new StringBuilder();
            row.append(CsvUtil.escapeCsv(question.getDifficulty().name()));
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getPrompt()));
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getOptions().get(0)));
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getOptions().get(1)));
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getOptions().get(2)));
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getOptions().get(3)));
            row.append(",");
            row.append(question.getCorrectIndex() + 1);
            row.append(",");
            row.append(CsvUtil.escapeCsv(question.getExplanation()));
            lines.add(row.toString());
        }

        try {
            Path target = Paths.get(path);
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.write(target, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Khong luu duoc file cau hoi: " + e.getMessage());
        }
    }

    private boolean looksLikeHeader(List<String> columns) {
        return !columns.isEmpty() && columns.get(0).toLowerCase().contains("difficulty");
    }

    private int parseCorrectIndex(String raw) {
        if (raw == null || raw.isEmpty()) {
            return -1;
        }

        if (raw.length() == 1) {
            char letter = Character.toUpperCase(raw.charAt(0));
            if (letter >= 'A' && letter <= 'D') {
                return letter - 'A';
            }
        }

        try {
            int numeric = Integer.parseInt(raw);
            if (numeric >= 1 && numeric <= 4) {
                return numeric - 1;
            }
        } catch (NumberFormatException ignored) {
        }

        return -1;
    }

    private List<Question> getFallbackQuestions() {
        List<Question> questions = new ArrayList<Question>();
        questions.add(new Question(
            Difficulty.EASY,
            "Ket qua cua 2 + 2 la bao nhieu?",
            new String[] { "3", "4", "5", "22" },
            1,
            "2 + 2 = 4."
        ));
        questions.add(new Question(
            Difficulty.MEDIUM,
            "Tro choi nay duoc viet bang ngon ngu nao?",
            new String[] { "Python", "Java", "C#", "Go" },
            1,
            "Ma nguon cua tro choi nam trong cac file .java."
        ));
        questions.add(new Question(
            Difficulty.HARD,
            "HTTP la viet tat cua cum tu nao?",
            new String[] {
                "HyperText Transfer Protocol",
                "High Task Transfer Platform",
                "Hyperlink Test Transport Process",
                "Host Transfer Tunnel Protocol"
            },
            0,
            "HTTP la viet tat cua HyperText Transfer Protocol."
        ));
        return questions;
    }
}

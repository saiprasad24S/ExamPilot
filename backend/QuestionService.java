import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionService {
    public QuestionService() {}

    public void addQuestionInteractive(Scanner sc) {
        System.out.print("Question text: ");
        String text = sc.nextLine().trim();
        List<String> opts = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            System.out.print("Option " + i + ": ");
            opts.add(sc.nextLine().trim());
        }
        System.out.print("Correct option (1-4): ");
        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
        Question q = new Question(text, opts, idx);
        InMemoryDatabase.getInstance().saveQuestion(q);
        System.out.println("Question saved.");
    }

    public List<Question> fetchFive() {
        List<Question> all = InMemoryDatabase.getInstance().questions();
        return all.subList(0, Math.min(5, all.size()));
    }

    public void listQuestions() {
        List<Question> all = InMemoryDatabase.getInstance().questions();
        int i = 1;
        for (Question q : all) {
            System.out.println("#" + (i++) + " " + q.getText());
            List<String> o = q.getOptions();
            for (int j = 0; j < o.size(); j++) System.out.println((j+1) + ") " + o.get(j));
            System.out.println("Correct: " + (q.getCorrectIndex()+1));
            System.out.println();
        }
    }
}

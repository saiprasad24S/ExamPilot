/**
 * QuizService.java - Service layer for quiz execution including starting quiz, recording answers, and calculating scores.
 * Manages the quiz flow from question display to result computation and storage.
 */

import java.util.List;
import java.util.Scanner;

public class QuizService {
    private QuestionService questionService;

    public QuizService(QuestionService qs) {
        this.questionService = qs;
    }

    public void startQuizInteractive(Scanner sc, User user) {
        List<Question> qs = questionService.fetchFive();
        if (qs.isEmpty()) { System.out.println("No questions available"); return; }
        int correct=0;
        for (int i=0;i<qs.size();i++) {
            Question q = qs.get(i);
            System.out.println("\nQ" + (i+1) + ": " + q.getText());
            for (int j=0;j<q.getOptions().size();j++) System.out.println((j+1)+") " + q.getOptions().get(j));
            System.out.print("Your answer (1-4): ");
            int a = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (a == q.getCorrectIndex()) correct++;
        }
        int total = qs.size();
        double pct = ((double) correct / total) * 100.0;
        System.out.println("\nResult: total=" + total + ", correct=" + correct + ", percent=" + String.format("%.2f", pct) + "%");
        Result r = new Result(user.getId(), total, correct, pct);
        InMemoryDatabase.getInstance().saveResult(r);
        System.out.println("Saved result.");
    }
}

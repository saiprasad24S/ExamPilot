/**
 * MainApp.java - Console-based test interface for quiz application with menu-driven user interaction.
 * Allows testing of registration, login, quiz taking, and admin question management without the web UI.
 */

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        QuestionService questionService = new QuestionService();
        QuizService quizService = new QuizService(questionService);

        while (true) {
            System.out.println("\n=== ExamPilot ===");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Admin Login");
            System.out.println("4) Exit");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();

            if ("1".equals(c)) userService.registerInteractive(sc);
            else if ("2".equals(c)) {
                User u = userService.loginInteractive(sc);
                if (u != null) quizService.startQuizInteractive(sc, u);
            }
            else if ("3".equals(c)) {
                System.out.print("Admin user: ");
                String au = sc.nextLine().trim();
                System.out.print("Admin pass: ");
                String ap = sc.nextLine().trim();
                if ("admin".equals(au) && "admin".equals(ap)) {
                    while (true) {
                        System.out.println("\nAdmin: 1) Add Question  2) View Questions  3) Back");
                        String ac = sc.nextLine().trim();
                        if ("1".equals(ac)) questionService.addQuestionInteractive(sc);
                        else if ("2".equals(ac)) questionService.listQuestions();
                        else if ("3".equals(ac)) break;
                        else System.out.println("Invalid");
                    }
                } else System.out.println("Invalid admin (use admin/admin)");
            }
            else if ("4".equals(c)) {
                System.out.println("Goodbye");
                sc.close();
                break;
            }
            else System.out.println("Invalid choice");
        }
    }
}

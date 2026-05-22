/**
 * InMemoryDatabase.java - Singleton database that stores all quiz data (users, questions, results) in memory.
 * Provides CRUD operations for users, questions, and results without requiring external database setup.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryDatabase {
    private static InMemoryDatabase instance;
    private List<User> users = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private List<Result> results = new ArrayList<>();

    private InMemoryDatabase() {
        users.add(new User("admin", "admin"));

        List<String> o1 = List.of("Banking services", "Digital engineering and consulting", "Retail operations", "Manufacturing");
        questions.add(new Question("What is Virtusa primarily known for?", o1, 1));

        List<String> o2 = List.of("Real estate investment", "Cloud and digital transformation", "Hotel management", "Retail distribution");
        questions.add(new Question("Which of the following is a core service offered by Virtusa?", o2, 1));

        List<String> o3 = List.of("Entertainment", "Banking, Healthcare, Communications, Media & Technology", "Agriculture", "Hospitality");
        questions.add(new Question("In which domain does Virtusa provide solutions?", o3, 1));

        List<String> o4 = List.of("Traditional paperwork", "Legacy system modernization and cloud adoption", "Manual operations", "Offline processes");
        questions.add(new Question("What is a key focus area for Virtusa in digital transformation?", o4, 1));

        List<String> o5 = List.of("Waterfall only", "Agile and DevOps methodologies", "No structured approach", "Manual coding without standards");
        questions.add(new Question("Which development approach does Virtusa commonly employ for software projects?", o5, 1));

        for (User u: users) if (u.getId()==null) u.setId(UUID.randomUUID().toString());
        for (Question q: questions) if (q.getId()==null) q.setId(UUID.randomUUID().toString());
    }

    public static InMemoryDatabase getInstance() {
        if (instance == null) instance = new InMemoryDatabase();
        return instance;
    }

    public List<User> users() { return users; }

    public List<Question> questions() { return questions; }

    public List<Result> results() { return results; }

    public User findUserByUsername(String username) {
        return users.stream().filter(u->u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public void saveUser(User u) {
        if (u.getId() == null) u.setId(UUID.randomUUID().toString());
        users.add(u);
    }

    public void saveQuestion(Question q) {
        if (q.getId() == null) q.setId(UUID.randomUUID().toString());
        questions.add(q);
    }

    public void saveResult(Result r) {
        results.add(r);
    }
}

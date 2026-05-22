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

        List<String> o1 = List.of("Berlin","Madrid","Paris","Rome");
        questions.add(new Question("What is the capital of France?", o1, 2));

        List<String> o2 = List.of("3","4","5","6");
        questions.add(new Question("2 + 2 = ?", o2, 1));

        List<String> o3 = List.of("Java","C#","Python","JavaScript");
        questions.add(new Question("Which language runs in a web browser?", o3, 3));

        List<String> o4 = List.of("Hyper Text Markup Language","Home Tool Markup Language","Hyperlinks Text Markup Language","Hyperlinking Text Markup Language");
        questions.add(new Question("HTML stands for?", o4, 0));

        List<String> o5 = List.of("Google","Facebook","Twitter","Microsoft");
        questions.add(new Question("Which company created React?", o5, 1));

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

import java.util.List;

public class Question {
    private String id;
    private String text;
    private List<String> options;
    private int correctIndex;

    public Question() {}

    public Question(String text, List<String> options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getText() { return text; }

    public List<String> getOptions() { return options; }

    public int getCorrectIndex() { return correctIndex; }

}

/**
 * Result.java - Data model class representing quiz result with user answers, score, and submission timestamp.
 * Stores quiz performance data including individual answers and final score calculation.
 */

import java.util.Date;

public class Result {
    private String id;
    private String userId;
    private String userEmail;
    private int total;
    private int correct;
    private double percentage;
    private Date takenAt;

    public Result() {}

    public Result(String userId, String userEmail, int total, int correct, double percentage) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.total = total;
        this.correct = correct;
        this.percentage = percentage;
        this.takenAt = new Date();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }

    public String getUserEmail() { return userEmail; }

    public int getTotal() { return total; }

    public int getCorrect() { return correct; }

    public double getPercentage() { return percentage; }

    public Date getTakenAt() { return takenAt; }

}

/**
 * User.java - Data model class representing a quiz application user with username and password authentication.
 * Contains user identity information and credentials for login/registration.
 */

public class User {
    private String id;
    private String username;
    private String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

}

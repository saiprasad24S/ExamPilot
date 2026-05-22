import java.util.Scanner;
import java.util.List;

public class UserService {
    public UserService() {}

    public void registerInteractive(Scanner sc) {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        if (InMemoryDatabase.getInstance().findUserByUsername(username) != null) {
            System.out.println("Username exists");
            return;
        }
        InMemoryDatabase.getInstance().saveUser(new User(username, password));
        System.out.println("Registered");
    }

    public User loginInteractive(Scanner sc) {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        User u = InMemoryDatabase.getInstance().findUserByUsername(username);
        if (u == null || !u.getPassword().equals(password)) {
            System.out.println("Invalid credentials");
            return null;
        }
        return u;
    }

    public void listUsers() {
        List<User> users = InMemoryDatabase.getInstance().users();
        for (User u: users) System.out.println(u.getUsername());
    }
}

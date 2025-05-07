import java.util.HashMap;

public class UserDatabase {
    private static HashMap<String, String> users = new HashMap<>();

    public static boolean addUser(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password);
        return true;
    }

    public static boolean validateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
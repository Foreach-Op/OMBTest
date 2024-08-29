package Security;

import java.util.HashMap;

public class UserManager {

    private static final HashMap<String, User> userHashMap = new HashMap<>();

    private UserManager(){}

    public static User getUser(String token) throws Exception {
        User user = userHashMap.get(token);
        if(user == null){
            throw new Exception("Active user not found.");
        }
        return user;
    }

    public static void saveUser(String token, User user) throws Exception {
        if(userHashMap.containsKey(token)){
            throw new Exception("User already exists.");
        }
        userHashMap.put(token, user);
    }

    public static boolean removeUser(String token){
        if(userHashMap.containsKey(token)){
            userHashMap.remove(token);
            return true;
        }
        return false;
    }
}

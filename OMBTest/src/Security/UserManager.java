package Security;

import Consume.ConsumerManager;
import Produce.ProducerManager;

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

    public boolean removeUser(String token){
        if(userHashMap.containsKey(token)){
            userHashMap.remove(token);
            return true;
        }
        switch (token.charAt(0)){
            case 'P':
                ProducerManager.getInstance().removeProducer(token);
                break;
            case 'C':
                ConsumerManager.getInstance().removeConsumer(token);
                break;
        }
        return false;
    }
}

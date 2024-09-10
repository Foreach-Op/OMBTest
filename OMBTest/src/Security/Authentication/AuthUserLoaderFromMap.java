package Security.Authentication;

import java.util.HashMap;

public class AuthUserLoaderFromMap implements AuthUserLoader {
    private final HashMap<String, String> usernamePasswordMap = new HashMap<>();
    public AuthUserLoaderFromMap(){
        usernamePasswordMap.put("admin", "admin");
        usernamePasswordMap.put("oguz", "oguz");
        usernamePasswordMap.put("broadcast", "broadcast");
    }
    @Override
    public AuthUser loadUserByUsername(String username) {
        String password = usernamePasswordMap.get(username);
        return new AuthUser(username, password);
    }
}

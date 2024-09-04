package Security.Authentication;

import Security.PasswordManager;

public class AuthenticationManager {

    public boolean checkUsernamePassword(String username, String password){
        PasswordManager passwordManager = new PasswordManager("");
        AuthUserLoader authUserLoader = new AuthUserLoaderFromMap();
        AuthUser authUser = authUserLoader.loadUserByUsername(username);
        String hashedPassword = passwordManager.encodePassword(password);
        return authUser.getPassword().equals(hashedPassword);
    }
}

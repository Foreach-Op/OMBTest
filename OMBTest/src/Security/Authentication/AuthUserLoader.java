package Security.Authentication;

public interface AuthUserLoader {
    AuthUser loadUserByUsername(String username);
}
